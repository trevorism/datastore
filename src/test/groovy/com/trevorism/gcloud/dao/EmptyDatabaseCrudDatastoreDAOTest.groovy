package com.trevorism.gcloud.dao

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.KeyFactory
import com.google.cloud.datastore.QueryResults
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertThrows

@MicronautTest
class EmptyDatabaseCrudDatastoreDAOTest {

    private final String kind = "TestSample"
    @Inject
    private CrudDatastoreDAO dao

    @Inject
    @Client("/")
    HttpClient httpClient

    def myData = []

    @BeforeEach
    void before(){
        dao.setKind(kind)
        def keyFactory = new KeyFactory("trevorism")
        dao.datastore = [newKeyFactory: { keyFactory },
                         put          : { obj ->
                             def entity = new Entity(obj)
                             def found = myData.find { it -> it.key.getId() == obj.key.getId() }
                             if (found) {
                                 myData.remove(found)
                                 myData << entity
                             } else {
                                 myData << entity
                             }
                             return entity
                         },
                         run          : { it -> myData as QueryResults },
                         delete       : { it -> myData.remove(0) },
                         get          : { Key key -> myData.find { it -> it.key.getId() == key.getId() } }
        ] as Datastore
    }

    @Test
    void testReadAllFromEmptyDB() {
        def results = dao.readAll()
        assert !results
    }

    @Test
    void testReadFromEmptyDB() {
        def result = dao.read(1)
        assert !result
    }

    @Test
    void testUpdateFromEmptyDB() {
        def jsonObject = ["name": "newName"]
        def result = dao.update(1, jsonObject)
        !result
    }

    @Test
    void testDeleteFromEmptyDB() {
        def result = dao.delete(1)
        !result
    }

    @Test
    void testCreateSimple() {
        def jsonObject = [:]
        jsonObject.put("name", "newName")

        def entity = dao.create(jsonObject)

        assert entity.get("name") == "newName"
    }

    @Test
    void testCreateWithId() {
        long id = 8

        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", id)

        def entity = dao.create(jsonObject)

        assert entity.get("name") == "sample"
        def readEntity = dao.read(id)

        assert entity.get("name") == readEntity.get("name")
    }

    @Test()
    void testCreateWithInvalidId() {
        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", "invalid")

        assertThrows(RuntimeException, () -> dao.create(jsonObject))

    }

}