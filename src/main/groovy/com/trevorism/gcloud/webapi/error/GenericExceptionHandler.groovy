package com.trevorism.gcloud.webapi.error

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Produces
@Singleton
@Requires(classes = [Exception, ExceptionHandler])
class GenericExceptionHandler implements ExceptionHandler<Exception, HttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(GenericExceptionHandler)

    @Override
    HttpResponse handle(HttpRequest request, Exception exception) {
        log.error("Unhandled error on {} {}", request?.method, request?.path, exception)
        String message = exception.message ?: exception.class.simpleName
        return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body([message: message])
    }
}
