package ru.kardel.core.util

import ru.kardel.core.model.Area
import java.util.zip.CRC32

fun crcHash(string: String) = CRC32().apply {
    update(string.toByteArray())
}.value.toString()

operator fun Area.plus(other: Area): Area {
    val x = minOf(x, other.x)
    val y = minOf(y, other.y)
    val right = maxOf(x + width, other.x + other.width)
    val bot = maxOf(y + height, other.y + other.height)
    return Area(x, y, right - x, bot - y)
}
