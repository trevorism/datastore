package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import jakarta.inject.Inject
import jakarta.inject.Singleton

import java.util.concurrent.ConcurrentHashMap

@Singleton
class DatastoreClientCache {

    private static final String DEFAULT_NAMESPACE = ""

    private final ConcurrentHashMap<String, Datastore> clientsByNamespace = new ConcurrentHashMap<>()
    private final Closure<Datastore> clientFactory

    @Inject
    DatastoreClientCache() {
        this({ String namespace -> httpOptionsForNamespace(namespace).build().getService() })
    }

    DatastoreClientCache(Closure<Datastore> clientFactory) {
        this.clientFactory = clientFactory
    }

    Datastore datastoreForNamespace(String namespace) {
        clientsByNamespace.computeIfAbsent(namespace ?: DEFAULT_NAMESPACE, clientFactory)
    }

    static DatastoreOptions.Builder httpOptionsForNamespace(String namespace) {
        DatastoreOptions.Builder builder = DatastoreOptions.newBuilder().setTransportOptions(DatastoreOptions.getDefaultHttpTransportOptions())
        if (namespace) {
            builder.setNamespace(namespace)
        }
        return builder
    }
}
