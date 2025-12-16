package com.salausmart.bookme_mcp_server

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDateTime

@Service
class BookmeClient(
    builder: RestClient.Builder,
    @Value("\${book-me.url.bookme-backend-url}") private var bookmeBackendUrl: String,
    @Value("\${book-me.auth.token}") private var authToken: String
) {

//    @Value("\${book-me.url.bookme-backend-url}")
//    private lateinit var bookmeBackendUrl: String

//    @Value("\${book-me.auth.token}")
//    private lateinit var authToken: String

    private val client: RestClient = builder
        .baseUrl(bookmeBackendUrl)
        .build()

    data class Reservation(
        val id: Long,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val createdBy: User?
    ) {
        data class User(
            val id: Long?,
            val name: String?
        )
    }

    fun bookRoom(roomId: Long, startTime: String, endTime: String): Result<Reservation>? {

        val requestBody = mapOf(
            "roomId" to roomId,
            "startTime" to startTime,
            "endTime" to endTime)

        val response = try {
            client.post()
                .uri("/reservation")
                .header("Authorization", "Bearer $authToken")
                .body(requestBody)
                .retrieve()
                .body<Reservation>()
        } catch (e: HttpClientErrorException) {
            return null
        }

        return response as Result<Reservation>?
    }
}