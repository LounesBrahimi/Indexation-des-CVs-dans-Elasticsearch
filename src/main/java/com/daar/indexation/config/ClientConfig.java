package com.daar.indexation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class ClientConfig extends AbstractElasticsearchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ClientConfig.class);

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration =
                ClientConfiguration.builder().connectedTo("localhost:9200").build();

        RestHighLevelClient client = RestClients.create(clientConfiguration).rest();

        String source =
                "{\"description\":\"Extract attachment information\",\"processors\":[{\"attachment\":{\"field\":\"data\"}}]}";

        PutPipelineRequest request = new PutPipelineRequest("attachment",
                new BytesArray(source.getBytes(StandardCharsets.UTF_8)), XContentType.JSON);

        try {
            client.ingest().putPipeline(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Pipeline setup failed", e.getMessage());
        }

        return client;
    }
}
