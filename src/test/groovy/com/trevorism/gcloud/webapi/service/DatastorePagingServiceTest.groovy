package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.QueryResults
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.webapi.model.paging.Page
import org.junit.jupiter.api.Test

import java.time.Instant
import java.time.temporal.ChronoUnit

class DatastorePagingServiceTest {

    @Test
    void testPageSize() {
        PagingService service = new DatastorePagingService()
        service.datastoreProvider = { ->
            { q -> [FullEntity.newBuilder(), FullEntity.newBuilder()] as QueryResults } as Datastore
        } as DatastoreProvider

        def results = service.page(new Page(page: 1, pageSize: 2), "CommentModel")
        assert results.size() == 2
    }

    @Test
    void testLimit() {
        PagingService service = new DatastorePagingService()
        service.datastoreProvider = { ->
            { q -> [FullEntity.newBuilder(), FullEntity.newBuilder()] as QueryResults } as Datastore
        } as DatastoreProvider

        def results = service.page(new Page(limit: 2), "CommentModel")
        assert results.size() == 2
    }
}
