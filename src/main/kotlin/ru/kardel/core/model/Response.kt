package ru.kardel.core.model

import java.time.LocalDateTime

data class SearchResponse(
    val id: Long,
    val search: Search
)

data class ProductResponse(
    val id: Long,
    val link: String,
    val name: Name,
    val price: Price,
    val area: Area,
    val inserted: LocalDateTime
)

data class ExecutionResponse(
    val id: Long,
    var links: List<String>,
    val inserted: LocalDateTime,
)

data class NewSearchResponse(val searchId: Long, val executionId: Long)
