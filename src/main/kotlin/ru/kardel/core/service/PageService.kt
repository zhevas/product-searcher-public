package ru.kardel.core.service

import com.google.cloud.vision.v1.Block
import com.google.cloud.vision.v1.Page
import org.springframework.stereotype.Service
import ru.kardel.core.configuration.properties.ApplicationProperties
import ru.kardel.core.model.BlockType
import ru.kardel.core.model.TypedBlock
import ru.kardel.core.util.getIfSomeWordContains
import ru.kardel.core.util.getIfSomeWordEquals
import ru.kardel.core.util.joinToString

private const val LENGTH_THRESHOLD = 200

@Service
class PageService(private val applicationProperties: ApplicationProperties) {

    fun getTypedBlocks(productNames: List<String>, page: Page): List<TypedBlock> {
        return page.blocksList.mapNotNull { it.getIfTyped(productNames) }
    }

    private fun Block.getIfTyped(productNames: List<String>): TypedBlock? {
        val words = paragraphsList.flatMap { it.wordsList }
        if (this.joinToString().length > LENGTH_THRESHOLD) return null
        val types: MutableList<BlockType> = mutableListOf()
        if (applicationProperties.currencies.any { words.getIfSomeWordEquals(it) != null }) {
            types.add(BlockType.PRICE)
        }
        productNames.forEach { productName ->
            if (productName.split(" ").all { words.getIfSomeWordContains(it) != null }) {
                types.add(BlockType.NAME)
            }
        }
        if (types.isEmpty()) return null
        return TypedBlock(types, this)
    }
}
