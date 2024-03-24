package com.trevorism.gcloud.webapi.controller

import com.google.cloud.datastore.Query
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.webapi.service.CrudDatastoreRepository
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.http.exceptions.HttpStatusException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Controller("/object")
class ObjectController {

    private static final Logger log = LoggerFactory.getLogger(ObjectController.class.name)

    @Inject
    CrudDatastoreRepository dao

    @Inject
    DatastoreProvider datastoreProvider

    @Tag(name = "Object Operations")
    @Operation(summary = "Get all types")
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<String> getKinds() {
        def query = Query.newKeyQueryBuilder().setKind("__kind__").build()
        def results = datastoreProvider.datastore.run(query)
        def list = []
        results.each {
            if (!it.getName().startsWith("_")) {
                list << it.getName()
            }
        }
        return list
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Get an object of type {kind} with id {id} **Secure")
    @Get(value = "{kind}/{id}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Map<String, Object> read(String kind, long id) {
        def entity = dao.read(kind, id)
        if (!entity)
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "${id} not found")

        return entity
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Get all objects of type {kind} **Secure")
    @Get(value = "{kind}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    List<Map<String, Object>> readAll(String kind) {
        def entities = dao.readAll(kind)
        return entities
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Create an object of type {kind} **Secure")
    @Status(HttpStatus.CREATED)
    @Post(value = "{kind}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Map<String, Object> create(String kind, @Body Map<String, Object> data) {
        try {
            def entity = dao.create(kind, data)
            return entity
        } catch (Exception e) {
            log.error("Unable to create ${kind}", e)
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Unable to create ${kind}")
        }
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Update an object of type {kind} with id {id} **Secure")
    @Put(value = "{kind}/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Map<String, Object> update(String kind, long id, @Body Map<String, Object> data) {
        def entity = dao.update(kind, id, data)
        if (!entity)
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "${id} not found")
        return entity
    }

    @Tag(name = "Object Operations")
    @Operation(summary = "Delete an object of type {kind} with id {id} **Secure")
    @Delete(value = "{kind}/{id}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    Map<String, Object> delete(String kind, long id) {
        def entity = dao.delete(kind, id)
        if (!entity)
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "${id} not found")
        return entity
    }

}
