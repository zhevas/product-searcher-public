package ru.kardel.core.service.report

import ru.kardel.core.model.Product

interface ReportService {
    fun buildReport(executionId: Long, entities: List<Product>): ByteArray
}
