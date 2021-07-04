package ru.kardel.core.model

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Parent
import java.time.Instant

@Entity(name = "search")
open class SearchDocument {
    @Id
    var id: Long? = null
    lateinit var search: Search
}

@Entity(name = "execution")
open class ExecutionDocument {
    @Id
    var id: Long? = null

    @Parent
    lateinit var searchKey: Key<SearchDocument>

    var links: MutableList<String> = mutableListOf()
    var inserted: Instant = Instant.now()
}

@Entity(name = "product")
open class ProductDocument {
    @Id
    var id: Long? = null

    @Parent
    lateinit var executionKey: Key<ExecutionDocument>

    lateinit var link: String
    lateinit var name: NameDocument
    lateinit var price: PriceDocument
    lateinit var area: AreaDocument
    val inserted: Instant = Instant.now()
}

open class NameDocument {
    lateinit var value: String
    lateinit var area: AreaDocument
}

open class PriceDocument {
    var value: Float? = null
    lateinit var currency: String
    lateinit var area: AreaDocument
}

open class AreaDocument {
    var x: Int = -1
    var y: Int = -1
    var width: Int = -1
    var height: Int = -1
}
