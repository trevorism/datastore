package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.KeyFactory
import com.google.cloud.datastore.QueryResults
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.bean.EntitySerializer
import com.trevorism.gcloud.webapi.model.paging.Page
import org.junit.jupiter.api.Test

import java.time.Instant
import java.time.temporal.ChronoUnit

class DatastorePagingServiceTest {

    def keyFactory = new KeyFactory("trevorism")

    @Test
    void testPageSize() {
        keyFactory.kind = "CommentModel"
        PagingService service = new DatastorePagingService()
        service.datastoreProvider = new TestDatastoreProvider([Entity.newBuilder(keyFactory.newKey(3)).build(),
                                                               Entity.newBuilder(keyFactory.newKey(4)).build()])
        service.entitySerializer = new EntitySerializer()
        def results = service.page(new Page(page: 1, pageSize: 2), "CommentModel")
        assert results.size() == 2
    }

    @Test
    void testLimit() {
        keyFactory.kind = "CommentModel"
        PagingService service = new DatastorePagingService()
        service.datastoreProvider = new TestDatastoreProvider([Entity.newBuilder(keyFactory.newKey(3)).build(),
                                                               Entity.newBuilder(keyFactory.newKey(4)).build()])
        service.entitySerializer = new EntitySerializer()
        def results = service.page(new Page(limit: 2), "CommentModel")
        assert results.size() == 2
    }
}
