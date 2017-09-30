package com.trevorism.gcloud.webapi.controller

import com.google.appengine.api.datastore.*
import com.trevorism.gcloud.dao.CrudDatastoreDAO
import com.trevorism.gcloud.dao.DatastoreDAO
import com.trevorism.gcloud.webapi.filter.Created
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import java.util.logging.Logger

@Api("CRUD Operations")
@Path("api")
class CrudController {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()
    private static final Logger log = Logger.getLogger(CrudController.class.name)

    @ApiOperation(value = "Get all types")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getEndpoints(){
        Query query = new Query(Entities.KIND_METADATA_KIND)
        def kindEntities = datastore.prepare(query).asIterable()

        def endpoints = []
        kindEntities.each {
            String endpoint = it.key.name
            if(!endpoint.startsWith("__"))
                endpoints << endpoint
        }
        return endpoints
    }

    @ApiOperation(value = "Get an object of type {kind} with id {id}")
    @GET
    @Path("{kind}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Entity read(@PathParam("kind") String kind, @PathParam("id") long id){
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.read(id)
        if(!entity)
            throw new NotFoundException()

        return entity
    }

    @ApiOperation(value = "Get all objects of type {kind}")
    @GET
    @Path("{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Entity> readAll(@PathParam("kind") String kind){
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entities = dao.readAll()
        return entities
    }


    @ApiOperation(value = "Create an object of type {kind} **Secure")
    @POST
    @Secure
    @Path("{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Created
    Entity create(@PathParam("kind") String kind, Map<String, Object> data){
        try {
            DatastoreDAO dao = new CrudDatastoreDAO(kind)
            def entity = dao.create(data)
            return entity
        }catch (Exception e){
            log.severe("Unable to create ${kind} object: ${data} :: ${e.getMessage()}")
            throw new BadRequestException(e)
        }
    }

    @ApiOperation(value = "Update an object of type {kind} with id {id} **Secure")
    @PUT
    @Secure
    @Path("{kind}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Entity update(@PathParam("kind") String kind, @PathParam("id") long id, Map<String, Object> data){
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.update(id, data)
        if(!entity)
            throw new NotFoundException()
        return entity

    }

    @ApiOperation(value = "Delete an object of type {kind} with id {id} **Secure")
    @DELETE
    @Secure
    @Path("{kind}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Entity delete(@PathParam("kind") String kind, @PathParam("id") long id){
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.delete(id)
        if(!entity)
            throw new NotFoundException()
        return entity
    }

}
