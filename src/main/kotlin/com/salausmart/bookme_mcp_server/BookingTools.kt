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
    ): String {

        return bookmeClient.bookRoom(roomId, startTime, endTime, authHeader = "Bearer ")
    }
}