package com.salausmart.bookme_mcp_server

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class AppConfig {

    @Bean
    fun restClient(): RestClient = RestClient.create()
}