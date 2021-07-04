package ru.kardel.core.controller

import com.googlecode.objectify.Key
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.kardel.core.mapping.toResponse
import ru.kardel.core.model.ExecutionResponse
import ru.kardel.core.model.SearchContext
import ru.kardel.core.model.SearchDocument
import ru.kardel.core.service.MainService
import ru.kardel.core.service.repository.GcdService

@RestController
@RequestMapping("/api/v1/search/{searchId}/execution")
@CrossOrigin
class ExecutionController(
    private val mainService: MainService,
    private val gcdService: GcdService,
    private val searchContext: SearchContext
) {

    @PostMapping
    fun addNewExecution(@PathVariable searchId: Long) {
        val searchDocument = gcdService.getSearch(searchId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        searchContext.apply {
            search = searchDocument.search
            searchKey = Key.create(SearchDocument::class.java, searchDocument.id!!)
        }
        mainService.process(searchContext.search)
    }

    @GetMapping
    fun getExecutions(@PathVariable searchId: Long): List<ExecutionResponse> {
        return gcdService.getExecutions(searchId).map { it.toResponse() }
    }

    @GetMapping("/{executionId}")
    fun getExecution(
        @PathVariable searchId: Long,
        @PathVariable executionId: Long
    ): ExecutionResponse {
        return gcdService.getExecution(executionId)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}
