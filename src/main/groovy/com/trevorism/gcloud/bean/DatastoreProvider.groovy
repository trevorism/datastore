package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import io.micronaut.http.HttpRequest
import io.micronaut.runtime.http.scope.RequestAware
import io.micronaut.runtime.http.scope.RequestScope

@RequestScope
class DatastoreProvider implements RequestAware {

    private String tenant
    private Datastore datastore

    Datastore getDatastore() {
        if (!datastore) {
            if(tenant){
                datastore = DatastoreOptions.newBuilder().setNamespace(tenant).build().getService()
            }
            else{
                datastore = DatastoreOptions.getDefaultInstance().getService()
            }
        }
        return datastore
    }

    @Override
    void setRequest(HttpRequest<?> request) {
        Optional<String> wrappedTenant = request.getAttribute("tenant", String)
        if(wrappedTenant.isPresent())
            tenant = wrappedTenant.get()
    }
}
