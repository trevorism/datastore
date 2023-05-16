package com.trevorism.gcloud.webapi.controller


import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Query
import com.trevorism.gcloud.dao.CrudDatastoreDAO
import com.trevorism.gcloud.dao.DatastoreDAO
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.hc.client5.http.HttpResponseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Controller("/object")
class ObjectController {

    private static final Logger log = LoggerFactory.getLogger(ObjectController.class.name)

    @Tag(name = "Object Operations")
    @Operation(summary = "Get all types")
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
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

    @Tag(name = "Object Operations")
    @Operation(summary = "Get an object of type {kind} with id {id}")
    @Get(value = "{kind}/{id}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Entity read(String kind, long id) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.read(id)
        if (!entity)
            throw new HttpResponseException(404, "${id} not found")

        return entity
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Get all objects of type {kind}")
    @Get(value = "{kind}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    List<Entity> readAll(String kind) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entities = dao.readAll()
        return entities
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Create an object of type {kind} **Secure")
    @Post(value = "{kind}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Entity create(String kind, @Body Map<String, Object> data) {
        try {
            DatastoreDAO dao = new CrudDatastoreDAO(kind)
            def entity = dao.create(data)
            return entity
        } catch (Exception e) {
            log.error("Unable to create ${kind}", e)
            throw new HttpResponseException(400, e.message)
        }
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Update an object of type {kind} with id {id} **Secure")
    @Put(value = "{kind}/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Entity update(String kind, long id, @Body Map<String, Object> data) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.update(id, data)
        if (!entity)
            throw new HttpResponseException(404, "${id} not found")
        return entity
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Delete an object of type {kind} with id {id} **Secure")
    @Delete(value = "{kind}/{id}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Entity delete(String kind, long id) {
        DatastoreDAO dao = new CrudDatastoreDAO(kind)
        def entity = dao.delete(id)
        if (!entity)
            throw new HttpResponseException(404, "${id} not found")
        return entity
    }

}
