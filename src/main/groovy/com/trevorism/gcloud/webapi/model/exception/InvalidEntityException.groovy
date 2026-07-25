package com.trevorism.gcloud.webapi.model.exception

class InvalidEntityException extends RuntimeException {

    InvalidEntityException(final String message) {
        super(message)
    }

    InvalidEntityException(final String message, final Throwable cause) {
        super(message, cause)
    }
}
