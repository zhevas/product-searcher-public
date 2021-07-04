package ru.kardel.core.service.content.consumers

import com.google.cloud.vision.v1.Page
import ru.kardel.core.model.Product

//TODO выделить отдельный тредпул для консюмеров и отдельный для основного процесса
interface LinksConsumer {
    fun consumeLinks(links: List<String>)
}

interface ImageConsumer {
    fun consumeImage(link: String, bytes: ByteArray)
}

interface PageConsumer {
    fun consumePage(link: String, page: Page)
}

interface ProductsConsumer {
    fun consumeProducts(products: List<Product>)
}

