package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Cursor
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.QueryResults
import com.google.datastore.v1.QueryResultBatch
import org.junit.jupiter.api.Test

class EntityListTest {

    @Test
    void testToList() {
        TestQueryResults results = [FullEntity.newBuilder().build(), FullEntity.newBuilder().build()] as TestQueryResults
        EntityList el = new EntityList(results)
        assert el.toList().size() == 2
    }

    class TestQueryResults extends ArrayList<Entity> implements QueryResults<Entity> {

        @Override
        Class<?> getResultClass() {
            Entity.class
        }

        @Override
        Cursor getCursorAfter() {
            return null
        }

        @Override
        int getSkippedResults() {
            return 0
        }

        @Override
        QueryResultBatch.MoreResultsType getMoreResults() {
            return null
        }

        @Override
        boolean hasNext() {
            iterator().hasNext()
        }

        @Override
        Entity next() {
            iterator().next()
        }
    }
}
