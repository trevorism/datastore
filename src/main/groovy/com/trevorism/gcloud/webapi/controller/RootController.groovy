package com.trevorism.gcloud.webapi.controller

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import io.swagger.annotations.Api

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api
@Path("/")
class RootController {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()

    @GET
    @Path("ping")
    @Produces(MediaType.APPLICATION_JSON)
    String ping(){
        if(datastore.getDatastoreAttributes().datastoreType)
            return "pong"
        return "gnop"
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getEndpoints(){
        return ["ping", "help", "api"]
    }

    @GET
    @Path("help")
    Response help(){
        Response.temporaryRedirect(new URI("/swagger")).build()
    }
}
