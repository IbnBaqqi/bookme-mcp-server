package com.salausmart.bookme_mcp_server

import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class AppConfig {

    @Bean
    fun restClient(): RestClient = RestClient.create()


    @Bean
    fun bookingToolService( bookingTools: BookingTools): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(bookingTools).build()
    }
}