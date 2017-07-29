package com.trevorism.gcloud.dao

import com.google.appengine.api.datastore.Entity
import com.trevorism.gcloud.LocalAppEngineTestBase
import org.junit.Test

/**
 * @author tbrooks
 */
class EmptyDatabaseCrudDatastoreDAOTest extends LocalAppEngineTestBase {

    private final String kind = "TestSample"

    @Test
    public void testReadAllFromEmptyDB() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def results = dao.readAll()
        assert !results
    }

    @Test
    public void testReadFromEmptyDB() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def result = dao.read(1)
        assert !result
    }

    @Test
    public void testUpdateFromEmptyDB() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def jsonObject = ["name": "newName"]
        def result = dao.update(1, jsonObject)
        !result
    }

    @Test
    public void testDeleteFromEmptyDB() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def result = dao.delete(1)
        !result
    }

    @Test
    public void testCreateSimple() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)
        def jsonObject = [:]
        jsonObject.put("name", "newName")

        Entity entity = dao.create(jsonObject)

        assert entity.getProperty("name") == "newName"
        assert !entity.getProperty("blah")

        Entity readEntity = dao.read(entity.key.id)

        assert entity.getProperty("name") == readEntity.getProperty("name")
        assert !readEntity.getProperty("blah")

    }

    @Test
    public void testCreateWithId() {
        long id = 8;
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)

        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", id)

        Entity entity = dao.create(jsonObject)

        assert entity.getProperty("name") == "sample"
        Entity readEntity = dao.read(id)

        assert entity.getProperty("name") == readEntity.getProperty("name")
    }

    @Test(expected = RuntimeException.class)
    public void testCreateWithInvalidId() {
        CrudDatastoreDAO dao = new CrudDatastoreDAO(kind)

        def jsonObject = [:]
        jsonObject.put("name", "sample")
        jsonObject.put("id", "invalid")

        Entity entity = dao.create(jsonObject)

        println entity
        println entity.key
        println entity.key.id

    }

}