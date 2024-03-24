package com.trevorism.gcloud.dao

import com.trevorism.gcloud.bean.DateFormatProvider
import com.trevorism.gcloud.webapi.service.CrudDatastoreRepository
import com.trevorism.gcloud.webapi.service.TestDatastoreProvider
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.text.DateFormat
import java.text.SimpleDateFormat


/**
 * @author tbrooks
 */
@MicronautTest
class CrudDatastoreDAOTest {

    private final String kind = "TestWithData"
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    private final def myData = []

    private final long id1 = 1
    private final long id2 = 2

    @Inject
    private CrudDatastoreRepository dao

    @BeforeEach
    void before() {
        dao.datastoreProvider = new TestDatastoreProvider()
        dao.dateFormatProvider = { -> dateFormat } as DateFormatProvider
    }

    @BeforeEach
    void setUp() {
        def jsonObject = [:]
        jsonObject.put("name", "sample1")
        jsonObject.put("id", id1)
        dao.create(kind, jsonObject)

        def jsonObject2 = [:]
        jsonObject2.put("name", "sample2")
        jsonObject2.put("id", id2)
        dao.create(kind, jsonObject2)
    }

    @Test
    void testSimple(){
        def list = dao.readAll(kind)
        assert list
    }

    @Test
    void testSimpleUpperCase(){
        def list = dao.readAll(kind.toUpperCase())
        assert list
    }

    @Test
    void testReadAll() {
        def results = dao.readAll(kind)
        assert results.size() == 2
    }

    @Test
    void testRead() {
        def result = dao.read(kind, id1)
        assert result.get("id") == id1
        assert result.get("name") == "sample1"

        def result2 = dao.read(kind, id2)

        assert result2.get("id") == id2
        assert result2.get("name") == "sample2"
    }

    @Test
    void testUpdate() {
        Map<String,Object> jsonObject = [:]
        jsonObject.put("name", "sample77")

        def result = dao.update(kind, id1, jsonObject)
        assert result.get("name") == "sample77"

        def result2 = dao.read(kind, id1)
        assert result2.get("name") == "sample77"
    }

    @Test
    void testUpdateWithId() {
        Map<String,Object> jsonObject = [:]
        jsonObject.put("name", "sample22")
        jsonObject.put("id", "invalid1")

        def result = dao.update(kind, id1, jsonObject)
        assert result.get("id") == id1

        def result2 = dao.read(kind, id1)
        assert result2.get("id") == id1
    }

    @Test
    void testDelete() {
        dao.delete(kind, id1)
        assert dao.readAll(kind).size() == 1
        dao.delete(kind, id2)
        assert !(dao.readAll(kind))
    }
}
