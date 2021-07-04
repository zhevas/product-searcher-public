package ru.kardel.core.service.repository

import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import org.springframework.stereotype.Service
import ru.kardel.core.mapping.toDocument
import ru.kardel.core.model.ExecutionDocument
import ru.kardel.core.model.Product
import ru.kardel.core.model.ProductDocument
import ru.kardel.core.model.Search
import ru.kardel.core.model.SearchContext
import ru.kardel.core.model.SearchDocument

@Service
class GcdService(
    private val searchContext: SearchContext
) : RepositoryService {
    override fun consumeLinks(links: List<String>) {
        val execution = ExecutionDocument().apply {
            this.links.addAll(links)
            this.searchKey = searchContext.searchKey
        }
        searchContext.executionKey = ofy().save().entity(execution).now()
    }

    override fun consumeProducts(products: List<Product>) {
        products.forEach { ofy().save().entity(it.toDocument().apply { executionKey = searchContext.executionKey }) }

    }

    override fun saveSearch(search: Search): Key<SearchDocument> {
        return ofy().save().entity(SearchDocument().apply { this.search = search }).now()
    }

    override fun getSearch(id: Long): SearchDocument? {
        return ofy().load().type(SearchDocument::class.java).id(id).now()
    }

    override fun getAllSearch(): List<SearchDocument> {
        return ofy().load().type(SearchDocument::class.java).list()
    }

    override fun getExecutions(searchId: Long): List<ExecutionDocument> {
        val ancestorKey = Key.create(SearchDocument::class.java, searchId)
        return ofy().load().type(ExecutionDocument::class.java).ancestor(ancestorKey).list()
    }

    override fun getExecution(executionId: Long): ExecutionDocument? {
        return ofy().load().type(ExecutionDocument::class.java).id(executionId).now()
    }

    override fun getProducts(searchId: Long, executionId: Long): List<ProductDocument> {
        val searchKey = Key.create(SearchDocument::class.java, searchId)
        val ancestorKey = Key.create(searchKey, ExecutionDocument::class.java, executionId)
        return ofy().load().type(ProductDocument::class.java).ancestor(ancestorKey).list()
    }

    override fun getProduct(productId: Long): ProductDocument? {
        return ofy().load().type(ProductDocument::class.java).id(productId).now()
    }

}
