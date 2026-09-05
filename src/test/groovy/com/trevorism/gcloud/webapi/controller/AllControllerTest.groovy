package com.trevorism.gcloud.webapi.controller

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.QueryResults
import com.google.cloud.datastore.StructuredQuery
import com.trevorism.gcloud.bean.DatastoreClientCache
import com.trevorism.gcloud.bean.DateFormatProvider
import com.trevorism.gcloud.bean.EntitySerializer
import org.junit.jupiter.api.Test

class AllControllerTest {

    private static final String PROJECT = "test-project"

    private final List<String> namespacesRequested = []

    @Test
    void testReadAllCombinesDefaultAndTenantNamespaces() {
        AllController controller = controllerWith([
                ""    : [widget(1, "default-one")],
                "acme": [widget(2, "acme-one"), widget(3, "acme-two")],
        ], ["acme"])

        List<Map<String, Object>> results = controller.readAll("Widget")

        assert results*.name == ["default-one", "acme-one", "acme-two"]
        assert results*.tenantId == [null, "acme", "acme"]
    }

    @Test
    void testReadAllSkipsTheUnnamedDefaultNamespaceKey() {
        AllController controller = controllerWith(["": []], [])

        controller.readAll("widget")

        assert namespacesRequested.every { it == "" }
    }

    @Test
    void testReadAllReusesCachedClientsPerNamespace() {
        AllController controller = controllerWith(["": [], "acme": [], "globex": []], ["acme", "globex"])

        controller.readAll("widget")
        controller.readAll("widget")

        assert namespacesRequested.count("") == 1
        assert namespacesRequested.count("acme") == 1
        assert namespacesRequested.count("globex") == 1
    }

    private AllController controllerWith(Map<String, List<Entity>> entitiesByNamespace, List<String> tenantNamespaces) {
        Closure<Datastore> factory = { String namespace ->
            namespacesRequested << namespace
            datastoreFor(entitiesByNamespace[namespace] ?: [], tenantNamespaces)
        }
        EntitySerializer serializer = new EntitySerializer()
        serializer.dateFormatProvider = new DateFormatProvider()

        AllController controller = new AllController()
        controller.entitySerializer = serializer
        controller.datastoreClientCache = new DatastoreClientCache(factory)
        return controller
    }

    private static Datastore datastoreFor(List<Entity> entities, List<String> tenantNamespaces) {
        [run: { StructuredQuery query ->
            if (query.kind == "__namespace__") {
                List<Key> keys = [Key.newBuilder(PROJECT, "__namespace__", 1L).build()]
                keys.addAll(tenantNamespaces.collect { Key.newBuilder(PROJECT, "__namespace__", it).build() })
                return queryResults(keys)
            }
            return queryResults(entities)
        }] as Datastore
    }

    private static QueryResults queryResults(List items) {
        Iterator iterator = items.iterator()
        [hasNext: { -> iterator.hasNext() }, next: { -> iterator.next() }] as QueryResults
    }

    private static Entity widget(long id, String name) {
        Entity.newBuilder(Key.newBuilder(PROJECT, "widget", id).build()).set("name", name).build()
    }
}
