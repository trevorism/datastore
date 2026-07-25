package com.trevorism.gcloud.webapi.service

import com.trevorism.gcloud.webapi.model.InvalidEntityException
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
    void testKeyColumnIsRejected() {
        CrudDatastoreRepository repository = new CrudDatastoreRepository()
        InvalidEntityException exception = assertThrows(InvalidEntityException,
                () -> repository.create("test", [key: "anything"]))
        assert exception.message.contains("cannot have a 'key' column")
    }
}
