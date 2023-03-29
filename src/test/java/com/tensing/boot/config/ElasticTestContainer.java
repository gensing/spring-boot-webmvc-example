package com.tensing.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

@Slf4j
@Profile("test")
@Configuration
public class ElasticTestContainer extends ElasticsearchConfiguration {

    @Container
    private static GenericContainer elasticsearchContainer;

    static {
        elasticsearchContainer = new GenericContainer(
                new ImageFromDockerfile()
                        .withDockerfileFromBuilder(builder -> {
                            builder
                                    // ES 이미지 가져오기
                                    .from("docker.elastic.co/elasticsearch/elasticsearch:7.15.2")
                                    // nori 분석기 설치
                                    .run("bin/elasticsearch-plugin install analysis-nori")
                                    .build();
                        })
        )
                .withExposedPorts(9200, 9300)
                .withClasspathResourceMapping("/docker/elasticsearch/config/dic", "/usr/share/elasticsearch/config/dic", BindMode.READ_ONLY)
                .withEnv("discovery.type", "single-node")
                .withReuse(true) // 컨테이너 리로드 X
        ;

        elasticsearchContainer.start();
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(String.format("%s:%d", elasticsearchContainer.getHost(), elasticsearchContainer.getMappedPort(9200)))
                .build();
    }

}