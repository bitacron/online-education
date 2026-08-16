package com.example.search.config;

import com.alibaba.fastjson.JSON;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.search.entity.User;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void contextLoads()  {
        System.out.println(restHighLevelClient);
    }


    @Test
    // 测试索引的创建（不带mapping，ElasticSearch默认会根据你的添加的文档来创建mapping）
    public void testCreateIndex() throws IOException {
        // 创建索引的请求
        CreateIndexRequest article = new CreateIndexRequest("article");
        // client执行请求
        CreateIndexResponse response = restHighLevelClient.indices().create(article, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    // 带上自定义的mapping来创建索引
    public void testCreateMappingIndex() throws IOException {
        // 创建索引的请求
        CreateIndexRequest indexRequest = new CreateIndexRequest("article");
        // mapping规则去别的地方写好之后，复制粘贴过来，IDEA会自动转义相关符号
        String mapping = "{\n" +
                "    \"properties\": {\n" +
                "      \"id\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "        \"index\": \"false\"\n" +
                "      },\n" +
                "      \"title\": {\n" +
                "        \"type\": \"text\"\n" +
                "        \"analyzer\": \"ik_max_word\"\n" +
                "      },\n" +
                "      \"content\": {\n" +
                "        \"type\": \"text\"\n" +
                "        \"analyzer\": \"ik_smart\"\n" +
                "      }\n" +
                "      \"cateId\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "    }\n" +
                "  }";
        // 添加索引的mapping规则
        indexRequest.mapping(mapping, XContentType.JSON);
        // 发送请求
        CreateIndexResponse response = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    // 测试索引是否存在
    public void testExitIndex() throws IOException {
        // 获取索引的请求
        GetIndexRequest new_index = new GetIndexRequest("article");
        // 执行请求
        boolean exists = restHighLevelClient.indices().exists(new_index, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    // 测试删除索引
    public void testDeleteIndex() throws IOException {
        // 删除索引的请求
        DeleteIndexRequest new_index = new DeleteIndexRequest("new_index");
        // 执行删除的请求
        AcknowledgedResponse response = restHighLevelClient.indices().delete(new_index, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    // 在索引中添加文档
    public void indexData() throws IOException {
        // 准备好数据
        User user = new User("carry", "男",13);
        // 创建好index请求
        IndexRequest indexRequest = new IndexRequest("users");
        // 设置索引
        indexRequest.id("1");
        // 设置超时时间（默认）
        indexRequest.timeout(TimeValue.timeValueSeconds(5));
        // 往请求中添加数据
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        //执行添加请求
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
    }

    @Test
    // 测试文档列表的查询（带条件）
    public void searchData() throws IOException {
        // 1.创建检索请求并指定索引
        SearchRequest searchRequest = new SearchRequest("users");

        // 构建搜索searchSourceBuilder 封装的条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构建查询条件(查询所有)
        //  MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        // 构建查询条件(精确匹配)
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("username", "carry");
        // 构造检索条件
        searchSourceBuilder.query(termsQueryBuilder);
        // 设置分页查询(跟sql语句的limit一样)
        searchSourceBuilder.from(0); // 开始下标(当前页码-1)*每页显示条数
        searchSourceBuilder.size(3); // 要查多少个
        // 设置排序规则
        searchSourceBuilder.sort("age", SortOrder.DESC);

        // 把所有条件设置给查询请求
        searchRequest.source(searchSourceBuilder);

        // 2.执行检索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 3.分析结果
        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println(totalHits);
        int length = hits.getHits().length;
        System.out.println(length);
        System.out.println("=============================================");
        for (SearchHit hit: hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    // 删除索引的文档
    public void deleteData() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("users","2");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
    }

    @Test
    // 测试文档的更新(id存在就是更新,id不存在就是添加) 局部更新，就是说只会覆盖其中有的字段
    public void testUpdateBetterDoc() throws IOException {
        //准备好修改的数据
        User user = new User("xao","男",18);
        // 创建更新请求
        UpdateRequest updateRequest = new UpdateRequest("users", "1");
        // 把要更新的数据装进去
        updateRequest.doc(JSON.toJSONString(user),XContentType.JSON);
        // 执行更新语句
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse);
    }

}
