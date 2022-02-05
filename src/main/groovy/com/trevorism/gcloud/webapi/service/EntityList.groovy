package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.QueryResults

class EntityList {

    private final QueryResults<Entity> results

    EntityList(QueryResults<Entity> results){
        this.results = results
    }

    List<Entity> toList(){
        results.collect ()
    }
}
