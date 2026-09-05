package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import jakarta.inject.Inject
import jakarta.inject.Singleton

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

@Singleton
class DatastoreClientCache {

    private static final String DEFAULT_NAMESPACE = ""

    private final ConcurrentHashMap<String, Datastore> clientsByNamespace = new ConcurrentHashMap<>()
    private final Function<String, Datastore> clientFactory

    @Inject
    DatastoreClientCache() {
        this(DatastoreClientCache::createClient)
    }

    DatastoreClientCache(Function<String, Datastore> clientFactory) {
        this.clientFactory = clientFactory
    }

    Datastore datastoreForNamespace(String namespace) {
        clientsByNamespace.computeIfAbsent(namespace ?: DEFAULT_NAMESPACE, clientFactory)
    }

    private static Datastore createClient(String namespace) {
        if (namespace) {
            return DatastoreOptions.newBuilder().setNamespace(namespace).build().getService()
        }
        return DatastoreOptions.getDefaultInstance().getService()
    }
}
