package com.trevorism.gcloud.webapi.controller


import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Query
import com.trevorism.gcloud.dao.CrudDatastoreDAO
import com.trevorism.gcloud.dao.DatastoreDAO
import com.trevorism.gcloud.webapi.filter.Created
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import java.util.logging.Logger

@Api("CRUD Operations")
@Path("api")
class CrudController {

    private static final Logger log = Logger.getLogger(CrudController.class.name)

    CrudController(){
        log.info("In CRUD controller constructor")
    }

    @ApiOperation(value = "Get all types")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getKinds() {
        def query = Query.newKeyQueryBuilder().setKind("__kind__").build()
        def results = DatastoreOptions.defaultInstance.getService().run(query)
        def list = []
        results.each {
            if (!it.getName().startsWith("_")) {
                list << it.getName()
            }
        }
        return list
    }

    @ApiOperation(value = "Get an object of type {kind} with id {id}")
    @GET
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{kind}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Entity read(@PathParam("kind") String kind, @PathParam("id") long id) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.read(id)
        if (!entity)
            throw new NotFoundException()

        return entity
    }

    @ApiOperation(value = "Get all objects of type {kind}")
    @GET
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Entity> readAll(@PathParam("kind") String kind) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entities = dao.readAll()
        return entities
    }


    @ApiOperation(value = "Create an object of type {kind} **Secure")
    @POST
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Created
    Entity create(@PathParam("kind") String kind, Map<String, Object> data) {
        try {
            DatastoreDAO dao = new CrudDatastoreDAO(kind)
            def entity = dao.create(data)
            return entity
        } catch (Exception e) {
            log.severe("Unable to create ${kind} object: ${data} :: ${e.getMessage()}")
            throw new BadRequestException(e)
        }
    }

    @ApiOperation(value = "Update an object of type {kind} with id {id} **Secure")
    @PUT
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{kind}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Entity update(@PathParam("kind") String kind, @PathParam("id") long id, Map<String, Object> data) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.update(id, data)
        if (!entity)
            throw new NotFoundException()
        return entity

    }

    @ApiOperation(value = "Delete an object of type {kind} with id {id} **Secure")
    @DELETE
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{kind}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Entity delete(@PathParam("kind") String kind, @PathParam("id") long id) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.delete(id)
        if (!entity)
            throw new NotFoundException()
        return entity
    }

}
