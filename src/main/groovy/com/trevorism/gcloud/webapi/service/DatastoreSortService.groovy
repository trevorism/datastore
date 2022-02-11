package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.EntityQuery
import com.google.cloud.datastore.StructuredQuery.OrderBy
import com.trevorism.gcloud.webapi.model.sorting.ComplexSort

class DatastoreSortService implements SortService {

    private Datastore datastore

    @Override
    def sort(ComplexSort request, String kind) {
        def queryBuilder = EntityQuery.Builder.newInstance().setKind(kind)

        def list = []
        request.sorts.each {
            if (it.descending) {
                list << OrderBy.desc(it.field)
            } else {
                list << OrderBy.asc(it.field)
            }
        }

        def entityQuery = queryBuilder.setOrderBy(*list).build()
        def results = getDatastore().run(entityQuery)
        new EntityList(results).toList()

    }

    private Datastore getDatastore() {
        if (!datastore)
            datastore = DatastoreOptions.getDefaultInstance().getService()
        return datastore
    }
}
