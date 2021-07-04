package ru.kardel.core.integration

import io.swagger.model.ConnectedPrediction
import io.swagger.model.DrawRequest
import io.swagger.model.ImageGroupRequest
import io.swagger.model.ImageGroupResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("vision-adapter", url = "\${rest.vision-adapter.base-url}")
interface VisionAdapterClient {

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["\${rest.vision-adapter.resources.prediction}"]
    )
    fun predict(request: String): List<ConnectedPrediction>

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["\${rest.vision-adapter.resources.predictionGroup}"]
    )
    fun predictGroup(request: ImageGroupRequest): ImageGroupResponse
}
