package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter
import com.trevorism.gcloud.webapi.service.DatastoreFilterService
import com.trevorism.gcloud.webapi.service.FilterService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api("Filter Operations")
@Path("filter")
class FilterController {

    private FilterService filterService = new DatastoreFilterService()

    @ApiOperation(value = "Perform a filter operation and get a result **Secure")
    @POST
    @Path("{kind}")
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    def operate(ComplexFilter filter, @PathParam("kind") String kind){
        filterService.filter(filter, kind)
    }

}
