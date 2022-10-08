package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.KeyFactory
import com.google.cloud.datastore.QueryResults
import com.google.cloud.datastore.StructuredQuery
import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter
import com.trevorism.gcloud.webapi.model.filtering.FilterConstants
import com.trevorism.gcloud.webapi.model.filtering.SimpleFilter
import org.junit.Test

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class DatastoreFilterServiceTest {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    DatastoreFilterServiceTest()
    {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    }

    @Test
    void testFilter() {
        DatastoreFilterService service = new DatastoreFilterService()
        service.datastore = { q -> [FullEntity.Builder.newInstance(), FullEntity.Builder.newInstance()] as QueryResults } as Datastore
        String dateString = dateFormat.format(Date.from(LocalDateTime.of(2012,1,1,0,0,0).toInstant(ZoneOffset.UTC)))

        def results = service.filter(new ComplexFilter(type: FilterConstants.AND, simpleFilters: [
                new SimpleFilter(type: FilterConstants.TYPE_DATE, field: "date", operator: "<", value: dateString)]), "CommentModel")

        assert results.size() == 2
    }

    @Test
    void testCreateSimpleFilter() {
        DatastoreFilterService service = new DatastoreFilterService()
        StructuredQuery.Filter filter = service.createSimpleFilter(
                new SimpleFilter(field: "test", operator: FilterConstants.OPERATOR_LESS_THAN, value: "string"),
                "kind"
        )

        assert filter.property == "test"
        assert filter.operator.toString() == "LESS_THAN"
        assert filter.value.get() == "string"
    }

    @Test
    void testCreateSimpleNumberFilter() {
        DatastoreFilterService service = new DatastoreFilterService()
        StructuredQuery.Filter filter = service.createSimpleFilter(
                new SimpleFilter(type: FilterConstants.TYPE_NUMBER, field: "num", operator: FilterConstants.OPERATOR_EQUAL, value: "44"),
                "kind"
        )

        assert filter.property == "num"
        assert filter.operator.toString() == "EQUAL"
        assert filter.value.get() == 44
    }

    @Test
    void testCreateSimpleBooleanFilter() {
        DatastoreFilterService service = new DatastoreFilterService()
        StructuredQuery.Filter filter = service.createSimpleFilter(
                new SimpleFilter(type: FilterConstants.TYPE_BOOLEAN, field: "bool", operator: FilterConstants.OPERATOR_LESS_THAN_OR_EQUAL, value: "true"),
                "kind"
        )

        assert filter.property == "bool"
        assert filter.operator.toString() == "LESS_THAN_OR_EQUAL"
        assert filter.value.get()
    }

    @Test
    void testCreateSimpleDateFilter() {
        DatastoreFilterService service = new DatastoreFilterService()
        def now = Instant.now()

        StructuredQuery.Filter filter = service.createSimpleFilter(
                new SimpleFilter(type: FilterConstants.TYPE_DATE, field: "date", operator: FilterConstants.OPERATOR_GREATER_THAN, value: dateFormat.format(now.toDate())), "kind")

        assert filter.property == "date"
        assert filter.operator.toString() == "GREATER_THAN"
        assert dateFormat.format(filter.value.get().toDate()) == dateFormat.format(now.toDate())
    }

    @Test
    void testCreateIdFilter() {
        DatastoreFilterService service = new DatastoreFilterService(){
            @Override
            Datastore getDatastore() {
                [newKeyFactory: { new KeyFactory("trevorism-gcloud")}] as Datastore
            }
        }
        def simpleFilter = new SimpleFilter(type: FilterConstants.TYPE_NUMBER, field: "id", operator: FilterConstants.OPERATOR_EQUAL, value: "59234923")
        StructuredQuery.Filter filter = service.createSimpleFilter(simpleFilter, "kind")

        assert filter.property == "__key__"
        assert filter.operator.toString() == "EQUAL"
        assert filter.value.get().id == 59234923
    }
}
