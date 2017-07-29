package com.trevorism.gcloud.webapi.controller

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class RootController {

    @GET
    @Path("ping")
    @Produces(MediaType.APPLICATION_JSON)
    String ping(){
        return "pong"
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getEndpoints(){
        return ["ping", "help", "api"]
    }

    @GET
    @Path("help")
    String help(){
        return """
GET /api -- List of all types<br/>
GET /api/{type} -- List of all object within a type<br/>
POST /api/{type} -- Create a new object of this type. Post the object in json format. <br/>
GET /api/{type}/{id} -- Get a type by id<br/>
PUT /api/{type}/{id} -- Update a type with the given id.<br/>
DELETE /api/{type}/{id} -- Delete an object with this id<br/>
"""
    }
}
