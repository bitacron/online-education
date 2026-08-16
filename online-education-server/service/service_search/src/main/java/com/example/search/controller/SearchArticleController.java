package com.example.search.controller;

import com.example.commonutils.R;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.search.service.SearchArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@RequestMapping("/edusearch/es")
@Api(value = "ElasticSearch相关接口", tags = {"ElasticSearch相关接口"})
@RestController
public class SearchArticleController {

    @Autowired
    private SearchArticleService searchArticleService;

    // 1.在索引中添加文档
    @ApiOperation(value = "在ElasticSearch索引中添加文档")
    @PostMapping("addDocument")
    public boolean addIndexDocument (@RequestBody ArticleElasticSearchModel searchArticle) throws IOException {
        System.out.println(searchArticle);
        boolean b = searchArticleService.addIndexDocument(searchArticle);
        return b;
    }
    // 2、获取这些数据实现搜索功能
    @ApiOperation(value = "ElasticSearch索引的搜索")
    @PostMapping("searchPageArticleCondition/{current}/{limit}")
    public R searchPage(@PathVariable int current, @PathVariable int limit, String keyWord) throws IOException {
        Map<String, Object> map = searchArticleService.searchPage(keyWord, current, limit);
        return R.ok().data(map);
    }
    // 3.删除索引的文档
    @ApiOperation(value = "删除ElasticSearch索引的文档")
    @DeleteMapping("deleteDocument/{id}")
    public boolean deleteIndexDocument (@PathVariable("id") String id) throws IOException {
        boolean b = searchArticleService.deleteIndexDocument(id);
        return b;
    }
    // 4.文档的更新(id存在就是更新,id不存在就是添加) 局部更新，就是说只会覆盖其中有的字段
    @ApiOperation(value = "更新ElasticSearch索引的文档")
    @PostMapping("updateDocument")
    public boolean updateIndexDocument(@RequestBody ArticleElasticSearchModel searchArticle) throws IOException {
        boolean b = searchArticleService.updateIndexDocument(searchArticle);
        return b;
    }

}
