package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.QueryResults
import com.trevorism.gcloud.bean.EntitySerializer

class EntityList {

    private final QueryResults<Entity> results
    private EntitySerializer entitySerializer

    EntityList(EntitySerializer entitySerializer, QueryResults<Entity> results){
        this.entitySerializer = entitySerializer
        this.results = results
    }

    List<Map<String, Object>> toList(){
        List<Map<String, Object>> list = []
        while(results.hasNext()){
            Entity entity = results.next()
            list << entitySerializer.serialize(entity)
        }
    }
}
