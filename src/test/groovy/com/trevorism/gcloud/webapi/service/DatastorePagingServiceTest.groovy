package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.QueryResults
import com.trevorism.gcloud.webapi.model.paging.Page
import org.junit.Test

class DatastorePagingServiceTest {

    @Test
    void testPageSize() {
        PagingService service = new DatastorePagingService()
        service.datastore = { q -> [FullEntity.Builder.newInstance(), FullEntity.Builder.newInstance()] as QueryResults } as Datastore

        def results = service.page(new Page(page: 1, pageSize: 2), "CommentModel")
        assert results.size() == 2
    }

    @Test
    void testLimit() {
        PagingService service = new DatastorePagingService()
        service.datastore = { q -> [FullEntity.Builder.newInstance(), FullEntity.Builder.newInstance()] as QueryResults } as Datastore

        def results = service.page(new Page(limit: 2), "CommentModel")
        assert results.size() == 2
    }
}
