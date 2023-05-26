package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.KeyFactory
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.bean.EntitySerializer
import com.trevorism.gcloud.webapi.model.sorting.ComplexSort
import com.trevorism.gcloud.webapi.model.sorting.Sort
import org.junit.jupiter.api.Test

import java.time.Instant
import java.time.temporal.ChronoUnit

class DatastoreSortServiceTest {

    def keyFactory = new KeyFactory("trevorism")

    @Test
    void testSort() {
        keyFactory.kind = "CommentModel"
        SortService service = new DatastoreSortService()
        service.datastoreProvider = new TestDatastoreProvider([Entity.newBuilder(keyFactory.newKey(3)).build(),
                                                               Entity.newBuilder(keyFactory.newKey(4)).build()])
        service.entitySerializer = new EntitySerializer()
        def results = service.sort(new ComplexSort(sorts: [new Sort(field: "date", descending: true)]), "CommentModel")
        assert results.size() == 2
    }
}
