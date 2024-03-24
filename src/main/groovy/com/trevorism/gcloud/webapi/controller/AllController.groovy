package com.trevorism.gcloud.webapi.controller

import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.EntityQuery
import com.google.cloud.datastore.Query
import com.trevorism.gcloud.bean.EntitySerializer
import com.trevorism.gcloud.webapi.service.EntityList
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Controller("/all")
class AllController {

    @Inject
    EntitySerializer entitySerializer

    @Tag(name = "Get All Operations")
    @Operation(summary = "Get all objects of type {kind} in all namespaces **Secure")
    @Get(value = "{kind}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM)
    List<Map<String, Object>> readAll(String kind) {
        List<Map<String, Object>> allResults = []
        kind = kind.toLowerCase()
        EntityQuery query = EntityQuery.newEntityQueryBuilder().setKind(kind).build()
        def results = DatastoreOptions.getDefaultInstance().getService().run(query)
        allResults.addAll(new EntityList(entitySerializer, results).toList())

        for(String namespace in getNamespaces()){
            def namespaceResult = DatastoreOptions.newBuilder().setNamespace(namespace).build().getService().run(query)
            List<Map<String, Object>> objects = new EntityList(entitySerializer, namespaceResult).toList()
            objects.each{
                it.put("tenantId", namespace)
            }
            allResults.addAll(objects)
        }

        return allResults
    }

    private static List getNamespaces() {
        def query = Query.newKeyQueryBuilder().setKind("__namespace__").build()
        def results = DatastoreOptions.getDefaultInstance().getService().run(query)
        def list = []
        while (results.hasNext()) {
            String name = results.next().getName()
            if(name)
                list.add(name)
        }
        return list
    }
}
