package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.sorting.ComplexSort
import com.trevorism.gcloud.webapi.service.DatastoreFilterService
import com.trevorism.gcloud.webapi.service.DatastoreSortService
import com.trevorism.gcloud.webapi.service.FilterService
import com.trevorism.gcloud.webapi.service.SortService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api("Sort Operations")
@Path("sort")
class SortController {

    private SortService sortService = new DatastoreSortService()

    @ApiOperation(value = "Perform a data operation and get a result **Secure")
    @POST
    @Path("{kind}")
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    def operate(ComplexSort complexSort, @PathParam("kind") String kind){
        sortService.sort(complexSort, kind)
    }

}
