package ru.kardel.core.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import ru.kardel.core.configuration.properties.GoogleProperties
import ru.kardel.core.configuration.properties.RendererProperties
import ru.kardel.core.configuration.properties.StorageProperties

@Configuration
class WebClientConfiguration {

    @Bean
    fun googleWebClient(properties: GoogleProperties) = builder()
        .baseUrl(properties.baseUrl)
        .defaultUriVariables(mapOf(
            "key" to properties.key,
            "cx" to properties.cx
        )).build()

    @Bean
    fun rendererWebClient(properties: RendererProperties) = builder()
        .baseUrl(properties.baseUrl)
        .build()

    @Bean
    fun storageWebClient(properties: StorageProperties) = builder()
        .baseUrl(properties.baseUrl)
        .build()

    private fun builder() = WebClient.builder().codecs { it.defaultCodecs().maxInMemorySize(5 * 1024 * 1024) }
}
