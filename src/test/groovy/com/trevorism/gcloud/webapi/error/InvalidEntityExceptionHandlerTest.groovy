package com.trevorism.gcloud.webapi.error

import com.trevorism.gcloud.webapi.model.exception.InvalidEntityException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Test

class InvalidEntityExceptionHandlerTest {

    @Test
    void testInvalidEntityIsABadRequest() {
        InvalidEntityExceptionHandler handler = new InvalidEntityExceptionHandler()
        HttpResponse response = handler.handle(request(), new InvalidEntityException("Invalid ID. ID must be a number instead of: true"))

        assert HttpStatus.BAD_REQUEST == response.status
    }

    @Test
    void testTheReasonIsPassedToTheCaller() {
        InvalidEntityExceptionHandler handler = new InvalidEntityExceptionHandler()
        HttpResponse response = handler.handle(request(), new InvalidEntityException("Invalid ID. ID must be a number instead of: true"))

        assert response.body()["message"].contains("must be a number")
    }

    private static HttpRequest request() {
        return [getPath: { "/object/test" }] as HttpRequest
    }
}
