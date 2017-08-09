package com.trevorism.gcloud.webapi.controller

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entities
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Query
import com.trevorism.gcloud.dao.CrudDatastoreDAO
import com.trevorism.gcloud.dao.DatastoreDAO
import com.trevorism.gcloud.webapi.filter.Created

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("api")
class CrudController {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService()


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

    @GET
    @Path("{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Entity> readAll(@PathParam("kind") String kind){
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entities = dao.readAll()
        return entities
    }


    @POST
    @Path("{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Created
    Entity create(@PathParam("kind") String kind, Map<String, Object> data){
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.create(data)
        return entity

    }

    @PUT
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

    @DELETE
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
