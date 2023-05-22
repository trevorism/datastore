package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.EntityQuery
import com.google.cloud.datastore.StructuredQuery.OrderBy
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.webapi.model.sorting.ComplexSort
import jakarta.inject.Inject

@jakarta.inject.Singleton
class DatastoreSortService implements SortService {

    @Inject
    DatastoreProvider datastoreProvider

    @Override
    def sort(ComplexSort request, String kind) {
        def queryBuilder = EntityQuery.Builder.newInstance().setKind(kind)

        def list = []
        request.sorts.each {
            if (it.descending) {
                list << OrderBy.desc(it.field.toLowerCase())
            } else {
                list << OrderBy.asc(it.field.toLowerCase())
            }
        }

        def entityQuery = queryBuilder.setOrderBy(*list).build()
        def results = datastoreProvider.getDatastore().run(entityQuery)
        new EntityList(results).toList()

    }

}
