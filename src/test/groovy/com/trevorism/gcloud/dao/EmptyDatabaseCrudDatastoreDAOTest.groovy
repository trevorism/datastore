package com.trevorism.gcloud.dao

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.KeyFactory
import com.google.cloud.datastore.QueryResults
import org.junit.Test

/**
 * @author tbrooks
 */
class EmptyDatabaseCrudDatastoreDAOTest {

    private final String kind = "TestSample"
    private final CrudDatastoreDAO dao
    def myData = []

    EmptyDatabaseCrudDatastoreDAOTest(){
        dao = new CrudDatastoreDAO(kind)
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

        Entity entity = dao.create(jsonObject)

        assert entity.getString("name") == "newName"
    }

    @Test
    void testCreateWithId() {
        long id = 8

        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", id)

        Entity entity = dao.create(jsonObject)

        assert entity.getString("name") == "sample"
        Entity readEntity = dao.read(id)

        assert entity.getString("name") == readEntity.getString("name")
    }

    @Test(expected = RuntimeException.class)
    void testCreateWithInvalidId() {
        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", "invalid")

        Entity entity = dao.create(jsonObject)

        println entity
        println entity.key
        println entity.key.id

    }

}