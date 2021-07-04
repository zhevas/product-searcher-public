package ru.kardel.core.service.content.providers

import com.google.cloud.vision.v1.Page
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import ru.kardel.core.integration.StorageClient
import ru.kardel.core.util.crcHash
import java.util.Base64

interface StorageContentProvider {
    fun getImage(executionId: Long, link: String): ByteArray?
    fun getPage(executionId: Long, link: String): Page?
}

@Service
@Profile("!storage")
class NoStorageContentProvider : StorageContentProvider {
    override fun getImage(executionId: Long, link: String): ByteArray? = null
    override fun getPage(executionId: Long, link: String): Page? = null
}

@Service
@Profile("storage")
class LocalStorageContentProvider(
    private val client: StorageClient,
) : StorageContentProvider {

    override fun getImage(executionId: Long, link: String): ByteArray? {
        return client.getImage(executionId, crcHash(link))?.let { Base64.getDecoder().decode(it) }
    }

    override fun getPage(executionId: Long, link: String): Page? {
        return client.getPage(executionId, crcHash(link))?.inputStream()?.use { Page.parseFrom(it) }
    }
}
