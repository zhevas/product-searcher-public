package ru.kardel.core.integration

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.Page
import com.google.protobuf.ByteString
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import ru.kardel.core.model.GooglePages
import ru.kardel.core.model.Search
import java.util.ArrayList

@Component
class GoogleClient(
    @Qualifier("googleWebClient") private val webClient: WebClient
) {

    fun google(search: Search, start: Int): GooglePages? = try {
        val exactTermsQuery = "\"${search.exactTerms.joinToString("\" \"")}\""
        val q = "\"${search.productName}\" $exactTermsQuery ${search.searchWords}"
        webClient.get()
            .uri(
                "?key={key}&cx={cx}&q={q}&start={start}&fileType=html,htm&gl=ru",
                mapOf("q" to q, "start" to start)
            )
            .retrieve()
            .bodyToMono(GooglePages::class.java)
            .block()
    } catch (ex: WebClientResponseException) {
        log.error(ex) { ex.responseBodyAsString }
        null
    }


    fun processImage(image: ByteArray): Page? {
        if (image.isEmpty()) return null
        return ImageAnnotatorClient.create().use { vision ->
            val img = image
                .let { ByteString.copyFrom(it) }
                .let { Image.newBuilder().setContent(it).build() }
            val requests: MutableList<AnnotateImageRequest> = ArrayList<AnnotateImageRequest>().also {
                val feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build()
                val request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build()
                it.add(request)
            }

            val pages = vision.batchAnnotateImages(requests).responsesList.asSequence()
                .onEach { if (it.hasError()) log.warn { "Response with error ${it.error.message}" } }
                .filterNot { it.hasError() }
                .mapNotNull { it.fullTextAnnotation.pagesList.firstOrNull() }
            if (pages.count() != 1) {
                log.error { "There are ${pages.count()} pages, must be 1" }
                null
            } else {
                pages.first()
            }
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
