package com.tripmark.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    RestClient restClient = RestClient.builder(
        new HttpHost("localhost", 9200, "http")
    ).build();

    RestClientTransport transport = new RestClientTransport(
        restClient,
        new JacksonJsonpMapper(objectMapper)
    );

    return new ElasticsearchClient(transport);
  }
}
