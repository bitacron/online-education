package com.example.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.search.service.SearchArticleService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SearchArticleServiceImpl implements SearchArticleService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    // 1.在索引中添加文档
    public boolean addIndexDocument (ArticleElasticSearchModel searchArticle) throws IOException {
        // 创建好index请求
        IndexRequest indexRequest = new IndexRequest("article");
        // 设置索引
        indexRequest.id(searchArticle.getId());
        // 设置超时时间（默认）
        indexRequest.timeout(TimeValue.timeValueSeconds(5));
        // 往请求中添加数据
        indexRequest.source(JSON.toJSONString(searchArticle), XContentType.JSON);
        //执行添加请求
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
        if (indexResponse != null) {
            return true;
        } else {
            return false;
        }
    }

    /*
    GET article/_search
        {
          "query": {
            "bool": {
              "should": [
                { "match": { "title": "java" }},
                { "match": { "content":  "java" }}
              ]
            }
          }
        }
     */
    // 2、获取这些数据实现搜索功能
    public Map<String, Object> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
        if (pageNo <= 1){
            pageNo = 1;
        }
        String lowerKeyWord = keyword.toLowerCase();
        // 1.创建检索请求并指定索引
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("article");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 构建查询条件(精确匹配)
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword.toLowerCase());
        //sourceBuilder.query(termQueryBuilder); // 构造检索条件
        // 构建查询条件(精确匹配)(多个条件)
        MatchQueryBuilder termQueryBuilder1 = QueryBuilders.matchQuery("title", lowerKeyWord);
        MatchQueryBuilder termQueryBuilder2 = QueryBuilders.matchQuery("content", lowerKeyWord);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(termQueryBuilder1).should(termQueryBuilder2);
        sourceBuilder.query(queryBuilder); // 构造检索条件
        // 设置分页查询(跟sql语句的limit一样)
        sourceBuilder.from((pageNo-1)*pageSize); // 开始下标(当前页码-1)*每页显示条数
        sourceBuilder.size(pageSize); // 要查多少个
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").field("content");
        highlightBuilder.requireFieldMatch(false);// 多个高亮显示！
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        // 把所有条件设置给查询请求
        searchRequest.source(sourceBuilder);

        // 2.执行检索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 3.解析结果
        ArrayList<ArticleElasticSearchModel> list = new ArrayList<>();
        long total = searchResponse.getHits().getTotalHits().value;
        for (SearchHit hit: searchResponse.getHits().getHits()) {
            String sourceAsString = hit.getSourceAsString();
            ArticleElasticSearchModel searchModel = JSON.parseObject(sourceAsString, ArticleElasticSearchModel.class);
            // 高亮
            if (StringUtils.isNotEmpty(lowerKeyWord)) {
                HighlightField title = hit.getHighlightFields().get("title");
                if (title != null) {
                    searchModel.setTitle(title.getFragments()[0].toString());
                }
                HighlightField content = hit.getHighlightFields().get("content");
                if (content != null) {
                    searchModel.setContent(content.getFragments()[0].toString());
                }
            }
            list.add(searchModel);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        long pages = 0; // 总页数
        if (total != 0) {
            if (total % pageSize == 0) {
                pages = total / pageSize;
            } else {
                pages = total / pageSize + 1;
            }
        }
        boolean hasNext = pageNo == pages ? false : true;
        boolean hasPrevious = pageNo == 1 ? false : true;
        Map<String, Object> map = new HashMap<>();
        map.put("items", list);
        map.put("total", total);
        map.put("current", pageNo);
        map.put("size", pageSize);
        map.put("pages", pages);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    // 3.删除索引的文档
    public boolean deleteIndexDocument(String articleId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("article",articleId);
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
        if (deleteResponse != null) {
            return true;
        } else {
            return false;
        }
    }

    // 4.文档的更新(id存在就是更新,id不存在就是添加) 局部更新，就是说只会覆盖其中有的字段
    public boolean updateIndexDocument(ArticleElasticSearchModel searchArticle) throws IOException {
        //准备好修改的数据

        // 创建更新请求
        UpdateRequest updateRequest = new UpdateRequest("article", searchArticle.getId());
        // 把要更新的数据装进去
        updateRequest.doc(JSON.toJSONString(searchArticle),XContentType.JSON);
        // 执行更新语句
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse);
        if (updateResponse != null) {
            return true;
        } else {
            return false;
        }
    }

}
