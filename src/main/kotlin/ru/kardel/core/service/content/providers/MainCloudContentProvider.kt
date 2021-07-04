package ru.kardel.core.service.content.providers

import com.google.cloud.vision.v1.Page
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import ru.kardel.core.integration.GoogleClient
import ru.kardel.core.model.Search


interface CloudContentProvider {
    fun makeImage(link: String): ByteArray?
    fun makeLinks(search: Search): List<String>
    fun readImage(bytes: ByteArray): Page?
}

@Service
@Profile("!debug")
class MainCloudContentProvider(
    private val googleClient: GoogleClient,
    private val imageProvider: ImageProvider
) : CloudContentProvider {

    private val starts = (1..91 step 10)

    override fun makeImage(link: String) = imageProvider.makeImage(link)

    override fun makeLinks(search: Search): List<String> {
        val result = mutableListOf<String>()
        for (start in starts) {
            googleClient.google(search, start)
                ?.let { response -> response.items.onEach { result.add(it.link) } }
                ?: return result
        }
        return result
    }

    override fun readImage(bytes: ByteArray): Page? = googleClient.processImage(bytes)
}

@Service
@Profile("debug")
class DebugCloudContentProvider : CloudContentProvider {
    override fun makeImage(link: String): ByteArray? = null

    override fun makeLinks(search: Search): List<String> = emptyList()

    override fun readImage(bytes: ByteArray): Page? = null
}
