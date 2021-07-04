package ru.kardel.core.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rest.google-search")
class GoogleProperties {
    lateinit var key: String
    lateinit var cx: String
    lateinit var baseUrl: String
}

@ConfigurationProperties(prefix = "rest.renderer")
class RendererProperties {
    lateinit var baseUrl: String
}

@ConfigurationProperties(prefix = "rest.storage")
class StorageProperties {
    lateinit var baseUrl: String
}
