package com.trevorism.gcloud.webapi.service

import com.google.cloud.datastore.Key
import com.trevorism.gcloud.bean.DateFormatProvider
import org.junit.jupiter.api.Test

class EntityIndexingTest {

    private static final Key KEY = Key.newBuilder("project", "kind", 1L).build()

    private static CrudDatastoreRepository repository() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        repository.dateFormatProvider = new DateFormatProvider()
        return repository
    }

    @Test
    void testFlattenedListIsNotIndexed() {
        def entity = repository().setEntityProperties(KEY, [tags: ["red", "blue"]])

        assert entity.getValue("tags").excludeFromIndexes()
        assert entity.getString("tags") == '["red","blue"]'
    }

    @Test
    void testFlattenedMapIsNotIndexed() {
        def entity = repository().setEntityProperties(KEY, [meta: [color: "red"]])

        assert entity.getValue("meta").excludeFromIndexes()
        assert entity.getString("meta") == '{"color":"red"}'
    }

    @Test
    void testShortStringStaysIndexed() {
        def entity = repository().setEntityProperties(KEY, [name: "Trev"])

        assert !entity.getValue("name").excludeFromIndexes()
        assert entity.getString("name") == "Trev"
    }

    @Test
    void testStringOverTheIndexLimitIsNotIndexed() {
        String long1501 = "a" * 1501
        def entity = repository().setEntityProperties(KEY, [notes: long1501])

        assert entity.getValue("notes").excludeFromIndexes()
        assert entity.getString("notes") == long1501
    }

    @Test
    void testStringAtTheIndexLimitStaysIndexed() {
        String exactly1500 = "a" * 1500
        def entity = repository().setEntityProperties(KEY, [notes: exactly1500])

        assert !entity.getValue("notes").excludeFromIndexes()
    }

    @Test
    void testLargeListIsStoredRatherThanRejected() {
        def bigList = (1..500).collect { "value-${it}".toString() }
        def entity = repository().setEntityProperties(KEY, [items: bigList])

        assert entity.getValue("items").excludeFromIndexes()
        assert entity.getString("items").getBytes("UTF-8").length > 1500
    }

    @Test
    void testNumbersAndBooleansAreUnaffected() {
        def entity = repository().setEntityProperties(KEY, [age: 10L, active: true])

        assert entity.getLong("age") == 10L
        assert entity.getBoolean("active")
        assert !entity.getValue("age").excludeFromIndexes()
    }
}
