package com.example.search.service;

import com.example.commonutils.vo.ArticleElasticSearchModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchArticleService {
    // 1.在索引中添加文档
    boolean addIndexDocument (ArticleElasticSearchModel searchArticle) throws IOException;
    // 2、获取这些数据实现搜索功能
    Map<String, Object> searchPage(String keyword, int pageNo, int pageSize) throws IOException;
    // 3.删除索引的文档
    boolean deleteIndexDocument(String articleId) throws IOException;
    // 4.文档的更新(id存在就是更新,id不存在就是添加) 局部更新，就是说只会覆盖其中有的字段
    boolean updateIndexDocument(ArticleElasticSearchModel searchArticle) throws IOException;
}
