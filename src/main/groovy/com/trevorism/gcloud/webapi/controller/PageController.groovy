package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.paging.Page
import com.trevorism.gcloud.webapi.service.DatastorePagingService
import com.trevorism.gcloud.webapi.service.PagingService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag


@Controller("/page")
class PageController {

    PagingService pagingService = new DatastorePagingService()

    @Tag(name = "Page Operations")
    @Operation(summary = "Perform a data operation and get a result **Secure")
    @Post(value = "/{kind}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    def operate(String kind, @Body Page paging){
        pagingService.page(paging, kind)
    }

}
