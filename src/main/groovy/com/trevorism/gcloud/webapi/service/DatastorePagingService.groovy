package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.EntityQuery
import com.trevorism.gcloud.webapi.model.paging.Page

class DatastorePagingService implements PagingService{

    private Datastore datastore

    @Override
    def page(Page pagingRequest, String kind) {
        def queryBuilder = EntityQuery.Builder.newInstance().setKind(kind)

        if(pagingRequest.limit != 0){
            queryBuilder = queryBuilder.setLimit(pagingRequest.limit)
        }
        if(pagingRequest.pageSize > 0 && pagingRequest.page > 0){
            queryBuilder = queryBuilder.setLimit(pagingRequest.pageSize).setOffset((pagingRequest.page-1) * pagingRequest.pageSize)
        }
        def results = getDatastore().run(queryBuilder.build())
        new EntityList(results).toList()
    }

    private Datastore getDatastore() {
        if (!datastore)
            datastore = DatastoreOptions.getDefaultInstance().getService()
        return datastore
    }
}
