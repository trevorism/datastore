package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.exception.InvalidEntityException
import com.trevorism.gcloud.webapi.service.DatastoreRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertThrows

class ObjectControllerTest {

    @Test
    void testCreateReturnsTheStoredEntity() {
        ObjectController controller = new ObjectController()
        controller.dao = [create: { String kind, Map<String, Object> data -> [id: 5L, name: "sample"] }] as DatastoreRepository

        assert 5L == controller.create("test", [name: "sample"])["id"]
    }

    @Test
    void testInvalidEntityPropagatesToTheExceptionHandler() {
        ObjectController controller = new ObjectController()
        controller.dao = [create: { String kind, Map<String, Object> data ->
            throw new InvalidEntityException("Invalid ID. ID must be a number instead of: true")
        }] as DatastoreRepository

        InvalidEntityException exception = assertThrows(InvalidEntityException, () -> controller.create("test", [id: true]))
        assert exception.message.contains("must be a number")
    }

    @Test
    void testUnexpectedFailurePropagatesUnchanged() {
        ObjectController controller = new ObjectController()
        controller.dao = [create: { String kind, Map<String, Object> data ->
            throw new IllegalStateException("datastore unavailable")
        }] as DatastoreRepository

        IllegalStateException exception = assertThrows(IllegalStateException, () -> controller.create("test", [:]))
        assert exception.message.contains("datastore unavailable")
    }

    @Test
    void testReadMissingEntityIsNotFound() {
        ObjectController controller = new ObjectController()
        controller.dao = [read: { String kind, long id -> null }] as DatastoreRepository

        HttpStatusException exception = assertThrows(HttpStatusException, () -> controller.read("test", 5))
        assert HttpStatus.NOT_FOUND == exception.status
    }

    @Test
    void testUpdateMissingEntityIsNotFound() {
        ObjectController controller = new ObjectController()
        controller.dao = [update: { String kind, long id, Map<String, Object> data -> null }] as DatastoreRepository

        HttpStatusException exception = assertThrows(HttpStatusException, () -> controller.update("test", 5, [name: "sample"]))
        assert HttpStatus.NOT_FOUND == exception.status
    }

    @Test
    void testDeleteMissingEntityIsNotFound() {
        ObjectController controller = new ObjectController()
        controller.dao = [delete: { String kind, long id -> null }] as DatastoreRepository

        HttpStatusException exception = assertThrows(HttpStatusException, () -> controller.delete("test", 5))
        assert HttpStatus.NOT_FOUND == exception.status
    }
}
