package com.trevorism.gcloud.webapi.error

import com.trevorism.gcloud.webapi.model.exception.InvalidEntityException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Produces
@Singleton
@Requires(classes = [InvalidEntityException, ExceptionHandler])
class InvalidEntityExceptionHandler implements ExceptionHandler<InvalidEntityException, HttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(InvalidEntityExceptionHandler)

    @Override
    HttpResponse handle(HttpRequest request, InvalidEntityException exception) {
        log.warn("Rejected invalid entity on {}: {}", request?.path, exception.message)
        return HttpResponse.badRequest([message: exception.message])
    }
}
