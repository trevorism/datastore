package com.trevorism.gcloud.webapi.controller



import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Api("Describe Operations")
@Path("describe")
class DescribeController {

    @ApiOperation(value = "Perform a data operation and get a result **Secure")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    def operate(def query){
        return ["list", "create", "read", "update", "delete", "filter", "page", "sort"]
    }

    @ApiOperation(value = "Get results of a saved data operation **Secure")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    def operateById(@PathParam("id") String id){
        return ["list", "create", "read", "update", "delete", "filter", "page", "sort"]
    }

    @ApiOperation(value = "Get results of a saved data operation **Secure")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    def describe(){
        return ["list", "create", "read", "update", "delete", "filter", "page", "sort"]
    }
}
