package ru.kardel.core.mapping

import ru.kardel.core.model.Area
import ru.kardel.core.model.AreaDocument
import ru.kardel.core.model.Name
import ru.kardel.core.model.NameDocument
import ru.kardel.core.model.Price
import ru.kardel.core.model.PriceDocument
import ru.kardel.core.model.Product
import ru.kardel.core.model.ProductDocument
import java.math.BigDecimal

fun Product.toDocument() = ProductDocument().also {
    it.link = link
    it.name = name.toDocument()
    it.price = price.toDocument()
    it.area = area.toDocument()
}


fun Name.toDocument() = NameDocument().also {
    it.value = value
    it.area = area.toDocument()
}

fun Price.toDocument() = PriceDocument().also {
    it.value = value.toFloat()
    it.currency = currency
    it.area = area.toDocument()
}

fun Area.toDocument() = AreaDocument().also {
    it.x = x
    it.y = y
    it.width = width
    it.height = height
}

fun NameDocument.toEntity() = Name(value, area.toEntity())

fun PriceDocument.toEntity() = Price(value?.toBigDecimal() ?: BigDecimal.ZERO, currency, area.toEntity())

fun ProductDocument.toEntity() = Product(link, name.toEntity(), price.toEntity(), area.toEntity())

fun AreaDocument.toEntity() = Area(x, y, width, height)

