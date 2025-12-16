package com.salausmart.bookme_mcp_server

import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookmeMcpServerApplication

fun main(args: Array<String>) {
	val context = runApplication<BookmeMcpServerApplication>(*args)

//	val response = context.getBean<BookingTools>().bookingRoom(1, "2025-12-17T20:00:00", "2025-12-17T20:30:00")
//	println(response)
}
