package ru.kardel.core.service.report

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import ru.kardel.core.model.Product
import java.io.ByteArrayOutputStream

@Service
class ExcelReportService : ReportService {
    override fun buildReport(executionId: Long, entities: List<Product>): ByteArray {
        XSSFWorkbook().use { excel ->
            val sheet = excel.createSheet(executionId.toString()).apply {
                createRow(0).apply {
                    createCell(0).setCellValue("Источник")
                    createCell(1).setCellValue("Наименование")
                    createCell(2).setCellValue("Цена")
                    createCell(3).setCellValue("Валюта")
                }
                setColumnWidth(1, 20000)
            }
            entities.forEach { sheet.writeProduct(it) }
            return ByteArrayOutputStream().apply { use { excel.write(it) } }.toByteArray()
        }
    }

    fun XSSFSheet.writeProduct(product: Product) {
        createRow(lastRowNum + 1).apply {
            createCell(0).setCellValue(product.link)
            createCell(1).setCellValue(product.name.value)
            createCell(2).setCellValue(product.price.value.toDouble())
            createCell(3).setCellValue(product.price.currency)
        }
    }
}


