package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.InvalidEntityException
import com.trevorism.gcloud.webapi.service.CrudDatastoreRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertThrows

class ObjectControllerTest {

    @Test
    void testInvalidEntityIsABadRequestCarryingTheReason() {
        ObjectController controller = controllerFailingWith(
                new InvalidEntityException("Invalid ID. ID must be a number instead of: true"))
        HttpStatusException exception = assertThrows(HttpStatusException, () -> controller.create("test", [id: true]))
        assert HttpStatus.BAD_REQUEST == exception.status
        assert exception.message.contains("must be a number")
    }

    @Test
    void testUnexpectedFailureStaysAGenericBadRequest() {
        ObjectController controller = controllerFailingWith(new IllegalStateException("datastore unavailable"))
        HttpStatusException exception = assertThrows(HttpStatusException, () -> controller.create("test", [:]))
        assert HttpStatus.BAD_REQUEST == exception.status
        assert exception.message.contains("Unable to create test")
    }

    private static ObjectController controllerFailingWith(RuntimeException failure) {
        ObjectController controller = new ObjectController()
        controller.dao = new CrudDatastoreRepository() {
            @Override
            Map<String, Object> create(String kind, Map<String, Object> data) {
                throw failure
            }
        }
        return controller
    }
}
