package com.trevorism.gcloud.bean

import com.google.cloud.NoCredentials
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.http.HttpTransportOptions
import org.junit.jupiter.api.Test

class DatastoreClientCacheTest {

    private int clientsCreated = 0
    private final Closure<Datastore> countingFactory = { String namespace ->
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

    @Test
    void testNamespacedClientUsesHttpTransport() {
        DatastoreOptions options = buildOptions("acme")

        assert options.transportOptions instanceof HttpTransportOptions
        assert options.namespace == "acme"
    }

    @Test
    void testDefaultClientUsesHttpTransport() {
        DatastoreOptions options = buildOptions(null)

        assert options.transportOptions instanceof HttpTransportOptions
        assert options.namespace == ""
    }

    private static DatastoreOptions buildOptions(String namespace) {
        DatastoreClientCache.httpOptionsForNamespace(namespace)
                .setProjectId("test-project")
                .setCredentials(NoCredentials.getInstance())
                .build()
    }
}
