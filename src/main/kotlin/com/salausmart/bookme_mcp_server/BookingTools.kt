package com.salausmart.bookme_mcp_server

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
class BookingTools(private val bookmeClient: BookmeClient) {


    @Tool(name = "book_meeting", description = "Book a meeting room")
    fun bookingRoom(
        @ToolParam roomId: Long,
        @ToolParam startTime: String,
        @ToolParam endTime: String
    ): String? {

        val bookingDetails = bookmeClient.bookRoom(roomId, startTime, endTime)
        val detailText = bookingDetails?.fold(
            {
            """
                roomId: ${it.id}
                startTime: ${it.startTime}
                endTime: ${it.endTime}
                createdBy: ${it.createdBy?.name}
            """.trimIndent()
        },
            {
                error -> "Booking failed: ${error.message}"
            }
        )
        return detailText
    }


    fun unavailableSlots() {

    }
}