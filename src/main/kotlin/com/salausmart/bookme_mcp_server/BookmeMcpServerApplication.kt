package com.salausmart.bookme_mcp_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookmeMcpServerApplication

fun main(args: Array<String>) {
	runApplication<BookmeMcpServerApplication>(*args)
}
