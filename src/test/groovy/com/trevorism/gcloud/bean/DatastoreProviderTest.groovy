package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Datastore
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.ServerAuthentication
import org.junit.jupiter.api.Test

class DatastoreProviderTest {

    private final List<String> requestedNamespaces = []
    private final Closure<Datastore> recordingFactory = { String namespace ->
        requestedNamespaces << namespace
        [toString: { -> "datastore:$namespace".toString() }] as Datastore
    }

    @Test
    void testTenantFromRequestSelectsNamespace() {
        DatastoreProvider provider = providerWithCache()
        provider.setRequest(requestAuthenticatedAs([tenant: "acme"]))

        provider.getDatastore()

        assert requestedNamespaces == ["acme"]
    }

    @Test
    void testNoTenantUsesDefaultNamespace() {
        DatastoreProvider provider = providerWithCache()
        provider.setRequest(requestAuthenticatedAs([:]))

        provider.getDatastore()

        assert requestedNamespaces == [""]
    }

    @Test
    void testUnauthenticatedRequestUsesDefaultNamespace() {
        DatastoreProvider provider = providerWithCache()
        provider.setRequest(HttpRequest.GET("/object/thing"))

        provider.getDatastore()

        assert requestedNamespaces == [""]
    }

    @Test
    void testProvidersForTheSameTenantShareOneClient() {
        DatastoreClientCache cache = new DatastoreClientCache(recordingFactory)
        DatastoreProvider firstRequest = providerWithCache(cache)
        DatastoreProvider secondRequest = providerWithCache(cache)
        firstRequest.setRequest(requestAuthenticatedAs([tenant: "acme"]))
        secondRequest.setRequest(requestAuthenticatedAs([tenant: "acme"]))

        assert firstRequest.getDatastore().is(secondRequest.getDatastore())
        assert requestedNamespaces == ["acme"]
    }

    private DatastoreProvider providerWithCache(DatastoreClientCache cache = new DatastoreClientCache(recordingFactory)) {
        DatastoreProvider provider = new DatastoreProvider()
        provider.datastoreClientCache = cache
        return provider
    }

    private static HttpRequest<?> requestAuthenticatedAs(Map<String, Object> attributes) {
        HttpRequest<?> request = HttpRequest.GET("/object/thing")
        request.setAttribute("micronaut.AUTHENTICATION", new ServerAuthentication("tester", [], attributes))
        return request
    }
}
