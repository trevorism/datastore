package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.EntityQuery
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.webapi.model.paging.Page
import jakarta.inject.Inject

@jakarta.inject.Singleton
class DatastorePagingService implements PagingService{

    @Inject
    DatastoreProvider datastoreProvider

    @Override
    def page(Page pagingRequest, String kind) {
        def queryBuilder = EntityQuery.Builder.newInstance().setKind(kind)

        if(pagingRequest.limit != 0){
            queryBuilder = queryBuilder.setLimit(pagingRequest.limit)
        }
        if(pagingRequest.pageSize > 0 && pagingRequest.page > 0){
            queryBuilder = queryBuilder.setLimit(pagingRequest.pageSize).setOffset((pagingRequest.page-1) * pagingRequest.pageSize)
        }
        def results = datastoreProvider.getDatastore().run(queryBuilder.build())
        new EntityList(results).toList()
    }

}
