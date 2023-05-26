package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.KeyFactory
import com.google.cloud.datastore.QueryResults
import com.trevorism.gcloud.bean.DatastoreProvider

class TestDatastoreProvider extends DatastoreProvider{

    private final def myData = []

    TestDatastoreProvider(def listOfEntities = null){
        if(listOfEntities){
            myData.addAll(listOfEntities)
        }
    }

    Datastore getDatastore() {
        def keyFactory = new KeyFactory("trevorism")
        [newKeyFactory: { keyFactory },
         put          : { obj ->
             def entity = new Entity(obj)
             def found = myData.find { it -> it.key.getId() == obj.key.getId() }
             if (found) {
                 myData.remove(found)
                 myData << entity
             } else {
                 myData << entity
             }
             return entity
         },
         run          : { it -> myData.listIterator() as QueryResults },
         delete       : { it -> myData.remove(0) },
         get          : { Key key -> myData.find { it -> it.key.getId() == key.getId() } }
        ] as Datastore
    }
}
