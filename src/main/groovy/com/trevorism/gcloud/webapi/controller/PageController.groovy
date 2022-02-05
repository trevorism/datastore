package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.paging.Page
import com.trevorism.gcloud.webapi.service.DatastorePagingService
import com.trevorism.gcloud.webapi.service.PagingService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api("Page Operations")
@Path("page")
class PageController {

    PagingService pagingService = new DatastorePagingService()

    @ApiOperation(value = "Perform a data operation and get a result **Secure")
    @POST
    @Path("{kind}")
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    def operate(Page paging, @PathParam("kind") String kind){
        pagingService.page(paging, kind)
    }

}
