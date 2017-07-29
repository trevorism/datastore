package com.trevorism.gcloud.dao

import com.trevorism.gcloud.LocalAppEngineTestBase
import org.junit.Before
import org.junit.Test

/**
 * @author tbrooks
 */
class CrudDatastoreDAOTest extends LocalAppEngineTestBase{

    private final String kind = "TestWithData"

    private final long id1 = 1
    private final long id2 = 2

    @Before
    void setUp() {
        super.setUp()
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)

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
    void testReadAll() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def results = dao.readAll()
        assert results.size() == 2
    }

    @Test
    void testRead() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def result = dao.read(id1)
        assert result.key.id == id1
        assert result.getProperty("name") == "sample1"

        def result2 = dao.read(id2)

        assert result2.key.id == id2
        assert result2.getProperty("name") == "sample2"
    }

    @Test
    void testUpdate() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)

        def jsonObject = [:]
        jsonObject.put("name", "sample77")

        def result = dao.update(id1, jsonObject)
        assert result.getProperty("name") == "sample77"

        def result2 = dao.read(id1)
        assert result2.getProperty("name") == "sample77"
    }

    @Test
    void testUpdateWithId() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)

        def jsonObject = [:]
        jsonObject.put("name", "sample22")
        jsonObject.put("id", "invalid1")

        def result = dao.update(id1, jsonObject)
        assert result.getProperty("id") == id1

        def result2 = dao.read(id1)
        assert result2.getProperty("id") == id1
    }

    @Test
    void testDelete() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)

        dao.delete(id1)
        assert dao.readAll().size() == 1
        dao.delete(id2)
        assert !(dao.readAll())
    }
}
