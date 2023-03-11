package com.tensing.boot.config;

import com.tensing.boot.config.properties.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@RequiredArgsConstructor
@Configuration
//@EnableElasticsearchRepositories // elasticsearch repository 허용
public class ElasticSearchConfiguration extends ElasticsearchConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchProperties.servers())
                .build();
    }

}