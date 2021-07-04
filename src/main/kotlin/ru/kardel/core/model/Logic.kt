package ru.kardel.core.model

import com.google.cloud.vision.v1.Block
import java.math.BigDecimal

data class TypedBlock(
    val types: List<BlockType>,
    val block: Block
)

enum class BlockType {
    NAME, PRICE
}

data class Product(
    val link: String,
    val name: Name,
    val price: Price,
    val area: Area
)

data class Price(
    val value: BigDecimal,
    val currency: String,
    val area: Area
)

data class Good(
    val type: String,
    val area: Area,
    val score: BigDecimal,
)

data class Name(
    val value: String,
    val area: Area
)

data class Area(val x: Int, val y: Int, val width: Int, val height: Int)
