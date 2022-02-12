package com.trevorism.gcloud.dao

import com.google.cloud.datastore.*
import org.junit.Before
import org.junit.Test

/**
 * @author tbrooks
 */
class CrudDatastoreDAOTest {

    private final String kind = "TestWithData"

    private final def myData = []

    private final long id1 = 1
    private final long id2 = 2

    private final CrudDatastoreDAO dao

    CrudDatastoreDAOTest() {
        def keyFactory = new KeyFactory("trevorism")
        dao = new CrudDatastoreDAO(kind)
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

    @Before
    void setUp() {
        def jsonObject = [:]
        jsonObject.put("name", "sample1")
        jsonObject.put("id", id1)
        dao.create(jsonObject)

        def jsonObject2 = [:]
        jsonObject2.put("name", "sample2")
        jsonObject2.put("id", id2)
        dao.create(jsonObject2)
    }

    @Test
    void testSimple(){
        def list = dao.readAll()
        assert list
    }

    @Test
    void testReadAll() {
        def results = dao.readAll()
        assert results.size() == 2
    }

    @Test
    void testRead() {
        def result = dao.read(id1)
        assert result.key.id == id1
        assert result.getString("name") == "sample1"

        def result2 = dao.read(id2)

        assert result2.key.id == id2
        assert result2.getString("name") == "sample2"
    }

    @Test
    void testUpdate() {
        def jsonObject = [:]
        jsonObject.put("name", "sample77")

        def result = dao.update(id1, jsonObject)
        assert result.getString("name") == "sample77"

        def result2 = dao.read(id1)
        assert result2.getString("name") == "sample77"
    }

    @Test
    void testUpdateWithId() {
        def jsonObject = [:]
        jsonObject.put("name", "sample22")
        jsonObject.put("id", "invalid1")

        def result = dao.update(id1, jsonObject)
        assert result.getLong("id") == id1

        def result2 = dao.read(id1)
        assert result2.getLong("id") == id1
    }

    @Test
    void testDelete() {
        dao.delete(id1)
        assert dao.readAll().size() == 1
        dao.delete(id2)
        assert !(dao.readAll())
    }
}
