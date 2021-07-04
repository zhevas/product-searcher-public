package ru.kardel.core.service

import org.springframework.stereotype.Service
import ru.kardel.core.model.Area
import ru.kardel.core.model.BlockType
import ru.kardel.core.model.Name
import ru.kardel.core.model.Price
import ru.kardel.core.model.Product
import ru.kardel.core.model.TypedBlock
import ru.kardel.core.util.plus
import java.awt.Point

//TODO вынести трешхолды в сервис
private const val DISTANCE_THRESHOLD = 1500

@Service
class MainEntitiesConnector(private val transformer: BlockTransformer) {

    //todo рассматривать в обе стороны пока 1 из списков не станет пустым (or not?)
    fun connect(productName: String, link: String, entities: List<TypedBlock>): List<Product> {
        val names = entities.asSequence()
            .filter { it.types.contains(BlockType.NAME) }
            .mapNotNull { transformer.transformToName(productName, it) }
            .toMutableList()
        val prices = entities.asSequence()
            .filter { it.types.contains(BlockType.PRICE) }
            .mapNotNull { transformer.transformToPrice(it) }
            .toList()
        return prices.mapNotNull { price ->
            price.findClosest(names)?.takeIf { it.findClosest(prices) == price }?.let { name ->
                names.remove(name)
                Product(link, name, price, name.area + price.area)
            }
        }
    }

    private fun Price.findClosest(names: List<Name>): Name? {
        return names
            .map { it.area.distance(this.area) to it }
            .minByOrNull { it.first }
            ?.takeIf { it.first < DISTANCE_THRESHOLD }
            ?.second
    }


    private fun Name.findClosest(prices: List<Price>): Price? {
        return prices
            .map { it.area.distance(this.area) to it }
            .minByOrNull { it.first }
            ?.takeIf { it.first < DISTANCE_THRESHOLD }
            ?.second
    }

    private fun Area.distance(other: Area): Double {
        val thisMiddle = this.middle()
        val otherMiddle = other.middle()
        return thisMiddle.distance(otherMiddle)
    }


    private fun Area.middle() = Point(x + width / 2, y + height / 2)

}
