package ru.kardel.core.integration

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import ru.kardel.core.configuration.properties.StorageResources


//TODO Refactor
@Component
class StorageClient(
    @Qualifier("storageWebClient") private val webClient: WebClient,
    private val resources: StorageResources
) {

    fun getImage(executionId: Long, linkId: String): String? {
        return try {
            webClient.get()
                .uri(resources.image, mapOf("searchId" to executionId, "linkId" to linkId))
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        } catch (ex: WebClientResponseException) {
            log.error(ex) { ex.responseBodyAsString }
            null
        }
    }

    fun getPage(executionId: Long, linkId: String): ByteArray? {
        return try {
            webClient.get()
                .uri(resources.page, mapOf("searchId" to executionId, "linkId" to linkId))
                .retrieve()
                .bodyToMono(ByteArray::class.java)
                .block()
        } catch (ex: WebClientResponseException) {
            log.error(ex) { ex.responseBodyAsString }
            null
        }
    }


    fun postPage(searchId: Long, linkId: String, base64Bytes: String): ByteArray? {
        return try {
            webClient.post()
                .uri(resources.page, mapOf("searchId" to searchId, "linkId" to linkId))
                .body(BodyInserters.fromPublisher(Mono.just(base64Bytes), String::class.java))
                .retrieve()
                .bodyToMono(ByteArray::class.java)
                .block()
        } catch (ex: WebClientResponseException) {
            log.error(ex) { ex.responseBodyAsString }
            null
        }
    }

    fun postImage(searchId: Long, linkId: String, base64Bytes: String): ByteArray? {
        return try {
            webClient.post()
                .uri(resources.image, mapOf("searchId" to searchId, "linkId" to linkId))
                .body(BodyInserters.fromPublisher(Mono.just(base64Bytes), String::class.java))
                .retrieve()
                .bodyToMono(ByteArray::class.java)
                .block()
        } catch (ex: WebClientResponseException) {
            log.error(ex) { ex.responseBodyAsString }
            null
        }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
