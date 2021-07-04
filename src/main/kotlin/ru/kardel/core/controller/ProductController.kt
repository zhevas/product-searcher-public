package ru.kardel.core.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.kardel.core.mapping.toResponse
import ru.kardel.core.model.ProductResponse
import ru.kardel.core.service.MainService
import ru.kardel.core.service.repository.GcdService

@RestController
@RequestMapping("/api/v1/search/{searchId}/execution/{executionId}/product")
@CrossOrigin
class ProductController(
    private val mainService: MainService,
    private val gcdService: GcdService
) {


    @GetMapping
    fun getProducts(
        @PathVariable searchId: Long,
        @PathVariable executionId: Long
    ): List<ProductResponse> {
        return gcdService.getProducts(searchId, executionId).map { it.toResponse() }
    }

    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable searchId: Long,
        @PathVariable executionId: Long,
        @PathVariable productId: Long
    ): ProductResponse {
        return gcdService.getProduct(productId)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/report")
    fun getReport(
        @PathVariable searchId: Long,
        @PathVariable executionId: Long
    ): ByteArray {
        return mainService.buildReport(searchId, executionId)
    }
}
