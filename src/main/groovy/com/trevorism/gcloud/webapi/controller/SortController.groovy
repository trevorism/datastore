package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.sorting.ComplexSort
import com.trevorism.gcloud.webapi.service.DatastoreSortService
import com.trevorism.gcloud.webapi.service.SortService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Controller("/sort")
class SortController {

    @Inject
    SortService sortService

    @Tag(name = "Sort Operations")
    @Operation(summary = "Perform a data operation and get a result **Secure")
    @Post(value = "/{kind}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    def operate(String kind, ComplexSort complexSort) {
        sortService.sort(complexSort, kind)
    }

}
