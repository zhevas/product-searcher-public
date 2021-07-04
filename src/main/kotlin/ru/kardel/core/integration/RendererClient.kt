package ru.kardel.core.integration

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

//TODO добавить логирование на клиенты
@Component
class RendererClient(@Qualifier("rendererWebClient") private val webClient: WebClient) {

    fun render(link: String): ByteArray? =
        try {
            webClient.post()
                .body(BodyInserters.fromValue(Request(link)))
                .retrieve()
                .bodyToMono(ByteArray::class.java)
                .block()
        } catch (ex: WebClientResponseException) {
            log.error(ex) { ex.responseBodyAsString }
            null
        } catch (ex: Exception) {
            log.error(ex) { "Unexpected exception" }
            null
        }


    companion object {
        private val log = KotlinLogging.logger { }
    }

}

private data class Request(
    val url: String,
    val output: String = "screenshot",
    val scrollPage: Boolean = true,
    val waitFor: Int = 5,
    val screenshot: Screenshot = Screenshot()
)

private data class Screenshot(val type: String = "jpeg")
