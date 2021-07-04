package ru.kardel.core.service

import org.springframework.stereotype.Service
import ru.kardel.core.mapping.toEntity
import ru.kardel.core.model.Search
import ru.kardel.core.model.SearchContext
import ru.kardel.core.service.content.consumers.ContentConsumersFacade
import ru.kardel.core.service.content.providers.CloudContentProvider
import ru.kardel.core.service.content.providers.StorageContentProvider
import ru.kardel.core.service.report.ReportService
import ru.kardel.core.service.repository.GcdService

@Service
class MainService(
    private val storageContentProvider: StorageContentProvider,
    private val mainCloudContentProvider: CloudContentProvider,
    private val contentConsumer: ContentConsumersFacade,
    private val pageService: PageService,
    private val mainTextEntitiesConnector: MainEntitiesConnector,
    private val reportService: ReportService,
    private val gcdService: GcdService,
    private val searchContext: SearchContext
) {

    fun buildReport(searchId: Long, executionId: Long): ByteArray {
        val products = gcdService.getProducts(searchId, executionId).map { it.toEntity() }
        return reportService.buildReport(executionId, products)
    }

    fun process(search: Search) {
        val links = mainCloudContentProvider.makeLinks(search).also { contentConsumer.consumeLinks(it) }
        links.forEach { processLink(it) }
    }

    private fun processLink(link: String) {
        val executionId = searchContext.executionKey.id
        val image = storageContentProvider.getImage(executionId, link)
            ?: mainCloudContentProvider.makeImage(link)
            ?: return
        contentConsumer.consumeImage(link, image)
        val page = storageContentProvider.getPage(executionId, link)
            ?: mainCloudContentProvider.readImage(image)
            ?: return
        contentConsumer.consumePage(link, page)
        val productNames = searchContext.search.let { it.alternativeNames.toMutableList().apply { add(it.productName) } }
        val typedBlocks = pageService.getTypedBlocks(productNames, page)
        val goodsEntities = mainTextEntitiesConnector.connect(searchContext.search.productName, link, typedBlocks)
        contentConsumer.consumeProducts(goodsEntities)
    }
}
