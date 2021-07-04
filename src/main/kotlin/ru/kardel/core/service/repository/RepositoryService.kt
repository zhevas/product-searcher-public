package ru.kardel.core.service.repository

import com.googlecode.objectify.Key
import ru.kardel.core.model.ExecutionDocument
import ru.kardel.core.model.ProductDocument
import ru.kardel.core.model.Search
import ru.kardel.core.model.SearchDocument
import ru.kardel.core.service.content.consumers.LinksConsumer
import ru.kardel.core.service.content.consumers.ProductsConsumer

interface RepositoryService : LinksConsumer, ProductsConsumer {
    fun saveSearch(search: Search): Key<SearchDocument>
    fun getSearch(id: Long): SearchDocument?
    fun getAllSearch(): List<SearchDocument>

    fun getExecutions(searchId: Long): List<ExecutionDocument>
    fun getExecution(executionId: Long): ExecutionDocument?

    fun getProducts(searchId: Long, executionId: Long): List<ProductDocument>
    fun getProduct(productId: Long): ProductDocument?
}
