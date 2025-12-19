package com.salausmart.bookme_mcp_server

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDateTime
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.ParameterizedTypeReference
import java.lang.reflect.ParameterizedType
import java.time.LocalDate

@Service
class BookingTools(
    builder: RestClient.Builder,
    @Value("\${book-me.url.bookme-backend-url}") private var bookmeBackendUrl: String,
    @Value("\${book-me.auth.token}") private var authToken: String
) {

    private val client: RestClient = builder
        .baseUrl(bookmeBackendUrl)
        .build()

    private val objectMapper = jacksonObjectMapper()

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

    data class Reserved(
        val id: Long,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val createdBy: Slot?
    ) {
        data class Slot(
            val id: Long?,
            val name: String?
        )
    }

    @Tool(name = "book_meeting", description = "Book a meeting room")
    fun bookingRoom(
        @ToolParam roomId: Long,
        @ToolParam startTime: String,
        @ToolParam endTime: String
    ): String? {

        val requestBody = mapOf(
            "roomId" to roomId,
            "startTime" to startTime,
            "endTime" to endTime)

        val reservationText = try {
            val reservation = client.post()
                    .uri("/reservation")
                    .header("Authorization", "Bearer $authToken")
                    .body(requestBody)
                    .retrieve()
                    .onStatus({ it.is4xxClientError} ) { _, response ->
                        val rawJson = response.body.bufferedReader().use { it.readText() } //read the text from the InputStream

                        // trying to extract the value from the error key in JSON
                        val errorMessage = try {
                            val map = objectMapper.readValue<Map<String, Any>>(rawJson)
                            map["error"]?.toString() ?: map.values.toString()
                        } catch(e: Exception) {
                            rawJson
                        }

                        throw RuntimeException(errorMessage)
                    }
                    .body<Reservation>()

            reservation?.let {
                """
                    Successfully booked!
                    roomId: ${it.id}
                    Start: ${it.startTime}
                    End: ${it.endTime}
                    createdBy: ${it.createdBy?.name}
                """.trimIndent()
            }
        } catch (e: Exception) {
            "Failed to book the room. ${e.message ?: "Unknown error"}"
        }

        return reservationText
    }


    @Tool(description = "To get the unavailable slot in a day or within days interval")
    fun unavailableSlots(
        @ToolParam start : LocalDate,
        @ToolParam end: LocalDate
    ) :String? {
        val requestBody = mapOf(
            "startTime" to start,
            "endTime" to end)

        val response = client.get()
            .uri("/reservation?start={start}&end={end}", start, end)
            .header("Authorization", authToken)
            .retrieve()
            .body(object: ParameterizedTypeReference<Reserved>)


        return null;
    }
}