package ru.kardel.core.mapping

import ru.kardel.core.model.ExecutionDocument
import ru.kardel.core.model.ExecutionResponse
import ru.kardel.core.model.ProductDocument
import ru.kardel.core.model.ProductResponse
import ru.kardel.core.model.SearchDocument
import ru.kardel.core.model.SearchResponse
import java.time.LocalDateTime
import java.time.ZoneId

fun SearchDocument.toResponse() = SearchResponse(
    id = id!!,
    search = search
)

fun ExecutionDocument.toResponse() = ExecutionResponse(
    id = id!!,
    links = links,
    inserted = LocalDateTime.ofInstant(inserted, ZoneId.systemDefault())
)

fun ProductDocument.toResponse() = ProductResponse(
    id = id!!,
    link = link,
    name = name.toEntity(),
    price = price.toEntity(),
    area = area.toEntity(),
    inserted = LocalDateTime.ofInstant(inserted, ZoneId.systemDefault())
)
