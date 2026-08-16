package com.example.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 配置类
public class ElasticSearchConfig {
    //public static final RequestOptions COMMON_OPTIONS;
    //
    //static {
    //    RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
    //    COMMON_OPTIONS = builder.build();
    //}

    /**
     * 主要是给容器中注入一个RestHighLevelClient
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("192.168.153.81", 9200,"http"));
        return new RestHighLevelClient(builder);
    }

}
