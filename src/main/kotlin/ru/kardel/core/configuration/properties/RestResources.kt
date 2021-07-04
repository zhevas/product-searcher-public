package ru.kardel.core.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rest.storage.resources")
class StorageResources {
    lateinit var image: String
    lateinit var page: String
}
