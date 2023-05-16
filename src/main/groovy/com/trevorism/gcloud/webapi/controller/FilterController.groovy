package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter
import com.trevorism.gcloud.webapi.service.DatastoreFilterService
import com.trevorism.gcloud.webapi.service.FilterService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag


@Controller("/filter")
class FilterController {

    private FilterService filterService = new DatastoreFilterService()

    @Tag(name = "Filter Operations")
    @Operation(summary = "Perform a filter data operation **Secure")
    @Post(value = "/{kind}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    def operate(String kind, @Body ComplexFilter filter){
        filterService.filter(filter, kind)
    }

}
