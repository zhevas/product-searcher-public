package ru.kardel.core.model

import com.googlecode.objectify.Key
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class SearchContext {
    lateinit var searchKey: Key<SearchDocument>
    lateinit var executionKey: Key<ExecutionDocument>
    lateinit var search: Search
}
