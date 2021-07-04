package ru.kardel.core.util

import com.google.cloud.vision.v1.Block
import com.google.cloud.vision.v1.BoundingPoly
import com.google.cloud.vision.v1.Paragraph
import com.google.cloud.vision.v1.Vertex
import com.google.cloud.vision.v1.Word
import ru.kardel.core.model.Area

fun BoundingPoly.toArea(): Area {
    with(verticesList) {
        val left = first().x
        val top = first().y
        val width = get(1).x - left
        val height = get(2).y - top
        return Area(left, top, width, height)
    }
}

fun Word.joinToString() = symbolsList.joinToString("") { it.text }
fun Paragraph.joinToString() = wordsList.joinToString(" ") { it.joinToString() }
fun Block.joinToString() = paragraphsList.joinToString("\n") { it.joinToString() }.toLowerCase()

fun List<Word>.getIfSomeWordContains(string: String) = find { it.joinToString().contains(string, true) }

fun List<Word>.getIfSomeWordEquals(string: String) = find {
    val word = it.joinToString()
    word.equals(string, true) || word.equals("$string.", true)
}

operator fun BoundingPoly.plus(other: BoundingPoly): BoundingPoly {
    val allVertices = this.verticesList + other.verticesList
    val left = allVertices.minByOrNull { it.x }!!.x
    val right = allVertices.maxByOrNull { it.x }!!.x
    val top = allVertices.minByOrNull { it.y }!!.y
    val bottom = allVertices.maxByOrNull { it.y }!!.y
    return BoundingPoly.newBuilder().apply {
        addVertices(Vertex.newBuilder().setX(left).setY(top).build())
        addVertices(Vertex.newBuilder().setX(right).setY(top).build())
        addVertices(Vertex.newBuilder().setX(right).setY(bottom).build())
        addVertices(Vertex.newBuilder().setX(left).setY(bottom).build())
    }.build()
}
