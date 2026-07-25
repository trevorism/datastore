package com.trevorism.gcloud.webapi.service

import com.trevorism.gcloud.webapi.model.exception.InvalidEntityException
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertThrows

class CrudDatastoreRepositoryTest {

    @Test
    void testNonNumericIdIsRejected() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        InvalidEntityException exception = assertThrows(InvalidEntityException,
                () -> repository.create("test", [id: true]))
        assert exception.message.contains("must be a number")
    }

    @Test
    void testFalsyNonNumericIdIsRejected() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        InvalidEntityException exception = assertThrows(InvalidEntityException,
                () -> repository.create("test", [id: false]))
        assert exception.message.contains("must be a number")
    }

    @Test
    void testEmptyIdIsRejected() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        InvalidEntityException exception = assertThrows(InvalidEntityException,
                () -> repository.create("test", [id: ""]))
        assert exception.message.contains("must be a number")
    }

    @Test
    void testNullIdPassesValidationAndIsTreatedAsAbsent() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        Exception exception = assertThrows(Exception,
                () -> repository.create("test", [id: null, name: "sample"]))
        assert !(exception instanceof InvalidEntityException)
    }

    @Test
    void testKeyColumnIsRejected() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        InvalidEntityException exception = assertThrows(InvalidEntityException,
                () -> repository.create("test", [key: "anything"]))
        assert exception.message.contains("cannot have a 'key' column")
    }

    @Test
    void testKeyColumnIsRejectedOnUpdate() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        InvalidEntityException exception = assertThrows(InvalidEntityException,
                () -> repository.update("test", 5, [key: "anything"]))
        assert exception.message.contains("cannot have a 'key' column")
    }
}
