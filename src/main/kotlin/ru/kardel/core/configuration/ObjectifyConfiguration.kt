package ru.kardel.core.configuration

import com.google.cloud.datastore.DatastoreOptions
import com.googlecode.objectify.ObjectifyFactory
import com.googlecode.objectify.ObjectifyFilter
import com.googlecode.objectify.ObjectifyService
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.kardel.core.model.ExecutionDocument
import ru.kardel.core.model.ProductDocument
import ru.kardel.core.model.SearchDocument
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener


@Configuration
class ObjectifyConfiguration {

    @Bean
    fun objectifyFilterRegistration(): FilterRegistrationBean<ObjectifyFilter>? {
        return FilterRegistrationBean<ObjectifyFilter>().apply {
            filter = ObjectifyFilter()
            addUrlPatterns("/*")
            order = 1
        }
    }

    @Bean
    fun listenerRegistrationBean(): ServletListenerRegistrationBean<ObjectifyListener>? {
        return ServletListenerRegistrationBean<ObjectifyListener>().apply {
            listener = ObjectifyListener()
        }
    }
}

@WebListener
class ObjectifyListener : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent) {
        val factory = ObjectifyFactory(DatastoreOptions.getDefaultInstance().service).apply { }
        ObjectifyService.init(factory)
        ObjectifyService.register(SearchDocument::class.java)
        ObjectifyService.register(ExecutionDocument::class.java)
        ObjectifyService.register(ProductDocument::class.java)
    }

    override fun contextDestroyed(sce: ServletContextEvent) {}
}

/*
class LocalDateTranslatorFactory : SimpleTranslatorFactory<LocalDate, Timestamp>(LocalDate::class.java, ValueType.TIMESTAMP) {
    override fun toPojo(value: Value<Timestamp>?): LocalDate? {
        return value?.get()
            ?.toDate()
            ?.toInstant()
            ?.atZone(ZoneId.systemDefault())
            ?.toLocalDate()
    }

    override fun toDatastore(value: LocalDate?): Value<Timestamp>? {
       return value?.let { Date.valueOf(it) }
            ?.let { Timestamp.of(it) }
            ?.let { TimestampValue.of(it) }
    }
}
*/
