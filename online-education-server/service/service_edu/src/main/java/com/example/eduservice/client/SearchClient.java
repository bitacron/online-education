package com.example.eduservice.client;

import com.example.commonutils.R;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.eduservice.client.Impl.OrderFileDegradeFeignClient;
import com.example.eduservice.client.Impl.SearchFileDegradeFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Component
@FeignClient(value = "service-search", fallback = SearchFileDegradeFeignClient.class)
public interface SearchClient {

    // 根据课程id和用户id，查询当前课程是否已经成功支付过
    @PostMapping("/edusearch/es/addDocument")
    public boolean addDocument(@RequestBody ArticleElasticSearchModel searchArticle);

    // 3.删除索引的文档
    @DeleteMapping("/edusearch/es/deleteDocument/{id}")
    public boolean deleteIndexDocument (@PathVariable("id") String id);

    // 4.文档的更新(id存在就是更新,id不存在就是添加) 局部更新，就是说只会覆盖其中有的字段
    @PostMapping("/edusearch/es/updateDocument")
    public boolean updateIndexDocument(@RequestBody ArticleElasticSearchModel searchArticle);
}
