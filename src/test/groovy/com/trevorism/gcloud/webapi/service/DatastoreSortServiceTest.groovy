package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.trevorism.gcloud.webapi.model.sorting.ComplexSort
import com.trevorism.gcloud.webapi.model.sorting.Sort
import org.junit.jupiter.api.Test

import java.time.Instant
import java.time.temporal.ChronoUnit

class DatastoreSortServiceTest {

    @Test
    void testSort() {
        SortService service = new DatastoreSortService()

        service.datastore = { q ->
            [
                    [id: 1, date: Date.from(Instant.now())],
                    [id: 2, date: Date.from(Instant.now().minus(1, ChronoUnit.DAYS))]
            ] as EntityListTest.TestQueryResults
        } as Datastore

        def results = service.sort(new ComplexSort(sorts: [new Sort(field: "date", descending: true)]), "CommentModel")
        assert results.size() == 2
    }
}
