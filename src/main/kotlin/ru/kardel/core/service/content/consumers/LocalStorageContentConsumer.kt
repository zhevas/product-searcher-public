package ru.kardel.core.service.content.consumers

import com.google.cloud.vision.v1.Page
import org.springframework.stereotype.Service
import ru.kardel.core.integration.StorageClient
import ru.kardel.core.model.SearchContext
import ru.kardel.core.util.crcHash
import java.io.ByteArrayOutputStream
import java.util.Base64


@Service
class LocalStorageContentConsumer(
    private val client: StorageClient,
    private val searchContext: SearchContext
) : ImageConsumer, PageConsumer {

    override fun consumeImage(link: String, bytes: ByteArray) {
        val base64Bytes = Base64.getEncoder().encodeToString(bytes)
        client.postImage(searchContext.executionKey.id, crcHash(link), base64Bytes)
    }

    override fun consumePage(link: String, page: Page) {
        val content = ByteArrayOutputStream().use { page.writeTo(it); it.toByteArray() }
        val base64Bytes = Base64.getEncoder().encodeToString(content)
        client.postPage(searchContext.executionKey.id, crcHash(link), base64Bytes)
    }
}

