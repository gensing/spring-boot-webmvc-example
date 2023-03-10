package com.tensing.boot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tensing.elasticsearch")
public record ElasticsearchProperties(
        String servers
) {
}
