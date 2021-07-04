package ru.kardel.core.service.content.providers

import org.springframework.stereotype.Service
import ru.kardel.core.integration.RendererClient

private const val IMAGE_SIZE_THRESHOLD = 100 * 1024

@Service
class ImageProvider(
    private val rendererClient: RendererClient
) {

    fun makeImage(link: String): ByteArray? =
        rendererClient.render(link)?.takeIf { it.size > IMAGE_SIZE_THRESHOLD }
}
