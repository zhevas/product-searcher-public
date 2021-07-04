package ru.kardel.core.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application")
class ApplicationProperties {
    lateinit var currencies: List<String>
}
