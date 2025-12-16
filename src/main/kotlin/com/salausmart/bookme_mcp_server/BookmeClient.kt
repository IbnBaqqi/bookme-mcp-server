package com.salausmart.bookme_mcp_server

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class BookmeClient(
    builder: RestClient.Builder,

) {

    @Value("\${url.bookme-backend-url}")
    private lateinit var bookmeBackendUrl: String

    private val client: RestClient = builder
        .baseUrl(bookmeBackendUrl)
        .build()

    fun bookRoom(roomId: Long, startTime: String, endTime: String, authHeader: String): String {

        val requestBody = mapOf("roomId" to roomId, "startTime" to startTime, endTime to endTime)
        return client.post()
            .uri("/reservation")
            .header("Authorization", authHeader)
            .body(requestBody)
            .retrieve()
            .body<String>() ?: "Booking failed"
    }
}