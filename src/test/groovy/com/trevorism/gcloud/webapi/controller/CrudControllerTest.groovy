package com.trevorism.gcloud.webapi.controller

import com.google.appengine.api.datastore.Entity
import com.trevorism.gcloud.LocalAppEngineTestBase
import org.junit.Test

import javax.ws.rs.NotFoundException

/**
 * @author tbrooks
 */
class CrudControllerTest extends LocalAppEngineTestBase {

    private static final String KIND = "Test"

    @Test
    void testRootController() {
        assert !(new CrudController().endpoints)
    }

    @Test
    void testCreateReadDelete() {
        CrudController crudController = new CrudController()
        Date now = new Date()

        Entity createdEntity = crudController.create(KIND, [name: "TEST", date: now, flag: true])
        assertTestEntity(createdEntity, now)

        Entity readEntity = crudController.read(KIND, createdEntity.key.id)
        assertTestEntity(readEntity, now)

        Entity deletedEntity = crudController.delete(KIND, createdEntity.key.id);
        assertTestEntity(deletedEntity, now)

        assert !crudController.readAll(KIND)
    }

    @Test
    void testCreateUpdateReadDelete() {
        CrudController crudController = new CrudController()
        Date now = new Date()

        Entity createdEntity = crudController.create(KIND, [name: "TEST", date: now, flag: true])
        assertTestEntity(createdEntity, now)

        Entity readEntity = crudController.update(KIND, createdEntity.key.id, [name: "Red", flag: false])
        assertUpdatedTestEntity(readEntity, now)

        Entity deletedEntity = crudController.delete(KIND, createdEntity.key.id);
        assertUpdatedTestEntity(deletedEntity, now)

        assert !crudController.readAll(KIND)
    }

    @Test
    void testCreateReadAllDelete() {
        CrudController crudController = new CrudController()
        Date now = new Date()

        Entity createdEntity = crudController.create(KIND, [name: "TEST", date: now, flag: true])
        assertTestEntity(createdEntity, now)

        Entity readEntity = crudController.readAll(KIND)[0]
        assertTestEntity(readEntity, now)

        Entity deletedEntity = crudController.delete(KIND, createdEntity.key.id);
        assertTestEntity(deletedEntity, now)

        assert !crudController.readAll(KIND)
    }

    @Test(expected = NotFoundException)
    void testMissingGet() {
        CrudController crudController = new CrudController()
        crudController.read(KIND, 1234)
    }

    @Test(expected = NotFoundException)
    void testMissingUpdate() {
        CrudController crudController = new CrudController()
        crudController.update(KIND, 1234, [:])
    }

    private static void assertTestEntity(Entity entity, Date date) {
        assert entity.key
        assert entity.properties["name"] == "TEST"
        assert entity.properties["date"] == date
        assert entity.properties["flag"]
    }

    private static void assertUpdatedTestEntity(Entity entity, Date date) {
        assert entity.key
        assert entity.properties["name"] == "Red"
        assert entity.properties["date"] == date
        assert !entity.properties["flag"]
    }
}
