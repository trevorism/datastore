package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Datastore
import io.micronaut.http.HttpRequest
import io.micronaut.runtime.http.scope.RequestAware
import io.micronaut.runtime.http.scope.RequestScope
import io.micronaut.security.authentication.ServerAuthentication
import jakarta.inject.Inject

@RequestScope
class DatastoreProvider implements RequestAware {

    @Inject
    DatastoreClientCache datastoreClientCache

    private String tenant

    Datastore getDatastore() {
        return datastoreClientCache.datastoreForNamespace(tenant)
    }

    @Override
    void setRequest(HttpRequest<?> request) {
        Optional<ServerAuthentication> wrappedTenant = request.getAttribute("micronaut.AUTHENTICATION", ServerAuthentication)
        if(wrappedTenant.isPresent())
            tenant = wrappedTenant.get()?.attributes?.get("tenant")
    }
}
