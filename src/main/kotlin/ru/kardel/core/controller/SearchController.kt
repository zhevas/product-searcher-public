package ru.kardel.core.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import ru.kardel.core.mapping.toResponse
import ru.kardel.core.model.NewSearchResponse
import ru.kardel.core.model.Search
import ru.kardel.core.model.SearchContext
import ru.kardel.core.model.SearchResponse
import ru.kardel.core.service.MainService
import ru.kardel.core.service.repository.GcdService
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin
class SearchController(
    private val mainService: MainService,
    private val gcdService: GcdService,
    private val searchContext: SearchContext
) {

    @GetMapping("/{searchId}")
    fun getSearch(@PathVariable searchId: Long): SearchResponse {
        return gcdService.getSearch(searchId)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @GetMapping
    fun getAllSearches(): List<SearchResponse> {
        return gcdService.getAllSearch().map { it.toResponse() }
    }

    @PostMapping
    fun createSearch(@Valid @RequestBody body: Search): NewSearchResponse {
        try {
            searchContext.search = body
            gcdService.saveSearch(body).let { searchContext.searchKey = it }
            mainService.process(body)
            return NewSearchResponse(searchContext.searchKey.id, searchContext.executionKey.id)
        } catch (ex: WebClientResponseException) {
            throw ResponseStatusException(ex.statusCode, "Google Response - ${ex.responseBodyAsString}", ex)
        }
    }
}
