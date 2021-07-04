package ru.kardel.core.service

import com.google.cloud.vision.v1.Word
import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.kardel.core.configuration.properties.ApplicationProperties
import ru.kardel.core.model.BlockType
import ru.kardel.core.model.Name
import ru.kardel.core.model.Price
import ru.kardel.core.model.TypedBlock
import ru.kardel.core.util.getIfSomeWordContains
import ru.kardel.core.util.getIfSomeWordEquals
import ru.kardel.core.util.joinToString
import ru.kardel.core.util.plus
import ru.kardel.core.util.toArea
import java.math.BigDecimal

@Service
class BlockTransformer(private val applicationProperties: ApplicationProperties) {

    fun transformToName(productName: String, block: TypedBlock): Name? {
        if (!block.types.contains(BlockType.NAME)) throw IllegalStateException("Not name block, can't transform")

        val nameParagraph = block.block.paragraphsList.find { paragraph ->
            productName.split(" ").all { paragraph.wordsList.getIfSomeWordContains(it) != null }
        } ?: return null

        return Name(
            value = nameParagraph.joinToString(),
            area = nameParagraph.boundingBox.toArea()
        )
    }

    private fun getPriceWords(currencyIndex: Int, allWords: List<Word>): List<Word> {
        val result = mutableListOf<Word>()
        var currentIndex = currencyIndex
        while (currentIndex > 0) {
            val prevWord = allWords[--currentIndex]
            if (prevWord.isPrice()) result.add(0, prevWord)
        }
        return result
    }

    private fun getCurrencyWords(currencyIndex: Int, allWords: List<Word>): List<Word> {
        val slashWord = allWords.getIfSomeWordContains("/") ?: return listOf(allWords[currencyIndex])
        val slashIndex = allWords.lastIndexOf(slashWord)
        if (slashIndex < currencyIndex || slashIndex - currencyIndex > 2 || slashIndex + 2 > allWords.size)
            return listOf(allWords[currencyIndex])
        return allWords.subList(currencyIndex, slashIndex + 2)
    }

    private fun List<Word>.toBigDecimal(): BigDecimal {
        var stringValue = joinToString("") { it.joinToString() }
            .replace(" ", "")
            .replace(",", ".")
        if (last().joinToString().length == 2) stringValue = stringValue.dropLast(2) + "." + stringValue.takeLast(2)
        return try {
            stringValue.toBigDecimal()
        } catch (ex: Exception) {
            log.warn { "Can not transform $stringValue to BigDecimal" }
            BigDecimal.ZERO
        }
    }


    fun transformToPrice(block: TypedBlock): Price? {
        if (!block.types.contains(BlockType.PRICE)) throw IllegalStateException("Not name block, can't transform")

        val allWords = block.block.paragraphsList.flatMap { it.wordsList }
        val currencyIndex = applicationProperties.currencies.asSequence()
            .mapNotNull { allWords.getIfSomeWordEquals(it) }
            .firstOrNull()
            ?.let { allWords.indexOf(it) }
            ?: return null

        val priceWords = getPriceWords(currencyIndex, allWords)
        if (priceWords.isEmpty()) return null
        val currencyWords = getCurrencyWords(currencyIndex, allWords)

        var area = allWords[currencyIndex].boundingBox
        priceWords.forEach { area += it.boundingBox }
        currencyWords.forEach { area += it.boundingBox }

        return Price(
            value = priceWords.toBigDecimal(),
            currency = currencyWords.joinToString("") { it.joinToString() },
            area = area.toArea()
        )
    }


    private fun Word.isPrice(): Boolean {
        val stringValue = joinToString()
        return stringValue.any { it.isDigit() } && stringValue.all { it.isDigit() || it.isDelimiter() || it.isWhitespace() }
    }

    private fun Char.isDelimiter() = this == ',' || this == '.'

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
