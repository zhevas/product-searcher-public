package ru.kardel.core.service.content.consumers

import com.google.cloud.vision.v1.Page
import org.springframework.stereotype.Service
import ru.kardel.core.model.Product

@Service
class ContentConsumersFacade(
    private val linksConsumers: List<LinksConsumer>,
    private val imageConsumers: List<ImageConsumer>,
    private val pageConsumers: List<PageConsumer>,
    private val productsConsumers: List<ProductsConsumer>,
) : LinksConsumer, ImageConsumer, PageConsumer, ProductsConsumer {
    override fun consumeLinks(links: List<String>) =
        linksConsumers.forEach { it.consumeLinks(links) }

    override fun consumeImage(link: String, bytes: ByteArray) =
        imageConsumers.forEach { it.consumeImage(link, bytes) }

    override fun consumePage(link: String, page: Page) =
        pageConsumers.forEach { it.consumePage(link, page) }

    override fun consumeProducts(products: List<Product>) =
        productsConsumers.forEach { it.consumeProducts(products) }

}
