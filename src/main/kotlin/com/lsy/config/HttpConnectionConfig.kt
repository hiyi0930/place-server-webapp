package com.lsy.config

import com.lsy.common.component.handler.KakaoResponseErrorHandler
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate


@Configuration
class HttpConnectionConfig {

    @Bean
    fun kakaoRestTemplate(): RestTemplate {
        val httpRequestFactory = HttpComponentsClientHttpRequestFactory()
        httpRequestFactory.setConnectTimeout(3000)
        httpRequestFactory.setReadTimeout(5000)
        val httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .build()
        httpRequestFactory.httpClient = httpClient as HttpClient
        val restTemplate = RestTemplate(httpRequestFactory)
        restTemplate.errorHandler= KakaoResponseErrorHandler()
        return RestTemplate(httpRequestFactory)
    }
}