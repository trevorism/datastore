package com.trevorism.gcloud.webapi.error

import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Test

class GenericExceptionHandlerTest {

    @Test
    void testUnexpectedFailureIsAServerError() {
        GenericExceptionHandler handler = new GenericExceptionHandler()
        HttpResponse response = handler.handle(request(), new IllegalStateException("datastore unavailable"))

        assert HttpStatus.INTERNAL_SERVER_ERROR == response.status
    }

    @Test
    void testTheReasonIsPassedToTheCaller() {
        GenericExceptionHandler handler = new GenericExceptionHandler()
        HttpResponse response = handler.handle(request(), new IllegalStateException("datastore unavailable"))

        assert response.body()["message"].contains("datastore unavailable")
    }

    @Test
    void testMessagelessFailureFallsBackToTheExceptionType() {
        GenericExceptionHandler handler = new GenericExceptionHandler()
        HttpResponse response = handler.handle(request(), new IllegalStateException())

        assert "IllegalStateException" == response.body()["message"]
    }

    private static HttpRequest request() {
        return [getMethod: { HttpMethod.POST }, getPath: { "/object/test" }] as HttpRequest
    }
}
