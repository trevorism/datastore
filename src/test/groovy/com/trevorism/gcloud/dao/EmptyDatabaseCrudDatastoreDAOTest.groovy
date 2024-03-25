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

import static org.junit.jupiter.api.Assertions.assertThrows

@MicronautTest
class EmptyDatabaseCrudDatastoreDAOTest {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    private final String kind = "TestSample"
    @Inject
    private CrudDatastoreRepository dao

    @BeforeEach
    void before(){
        dao.datastoreProvider = new TestDatastoreProvider()
        dao.dateFormatProvider = { -> dateFormat } as DateFormatProvider
    }

    @Test
    void testReadAllFromEmptyDB() {
        def results = dao.readAll(kind)
        assert !results
    }

    @Test
    void testReadFromEmptyDB() {
        def result = dao.read(kind, 1)
        assert !result
    }

    @Test
    void testUpdateFromEmptyDB() {
        def jsonObject = ["name": "newName"]
        def result = dao.update(kind, 1, jsonObject)
        !result
    }

    @Test
    void testDeleteFromEmptyDB() {
        def result = dao.delete(kind, 1)
        !result
    }

    @Test
    void testCreateSimple() {
        def jsonObject = [:]
        jsonObject.put("id", 123412)
        jsonObject.put("name", "newName")

        def entity = dao.create(kind, jsonObject)

        assert entity.get("name") == "newName"
    }

    @Test
    void testCreateWithId() {
        long id = 8

        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", id)

        def entity = dao.create(kind, jsonObject)

        assert entity.get("name") == "sample"
        def readEntity = dao.read(kind, id)

        assert entity.get("name") == readEntity.get("name")
    }

    @Test()
    void testCreateWithInvalidId() {
        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", "invalid")

        assertThrows(RuntimeException, () -> dao.create(kind, jsonObject))

    }

}