package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Datastore
import org.junit.jupiter.api.Test

import java.util.function.Function

class DatastoreClientCacheTest {

    private int clientsCreated = 0
    private final Function<String, Datastore> countingFactory = { String namespace ->
        clientsCreated++
        [toString: { -> "datastore:$namespace".toString() }] as Datastore
    }

    @Test
    void testSameNamespaceReusesClient() {
        DatastoreClientCache cache = new DatastoreClientCache(countingFactory)

        Datastore first = cache.datastoreForNamespace("acme")
        Datastore second = cache.datastoreForNamespace("acme")

        assert first.is(second)
        assert clientsCreated == 1
    }

    @Test
    void testDifferentNamespacesGetDifferentClients() {
        DatastoreClientCache cache = new DatastoreClientCache(countingFactory)

        Datastore acme = cache.datastoreForNamespace("acme")
        Datastore globex = cache.datastoreForNamespace("globex")

        assert !acme.is(globex)
        assert clientsCreated == 2
    }

    @Test
    void testNullAndEmptyNamespaceShareTheDefaultClient() {
        DatastoreClientCache cache = new DatastoreClientCache(countingFactory)

        Datastore fromNull = cache.datastoreForNamespace(null)
        Datastore fromEmpty = cache.datastoreForNamespace("")

        assert fromNull.is(fromEmpty)
        assert clientsCreated == 1
    }
}
