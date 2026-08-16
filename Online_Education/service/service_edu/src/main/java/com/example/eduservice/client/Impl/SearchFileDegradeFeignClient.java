package com.example.eduservice.client.Impl;

import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.eduservice.client.SearchClient;
import org.springframework.stereotype.Component;

@Component
public class SearchFileDegradeFeignClient implements SearchClient {
    @Override
    public boolean addDocument(ArticleElasticSearchModel searchArticle) {
        return false;
    }

    @Override
    public boolean deleteIndexDocument(String id) {
        return false;
    }

    @Override
    public boolean updateIndexDocument(ArticleElasticSearchModel searchArticle) {
        return false;
    }
}
