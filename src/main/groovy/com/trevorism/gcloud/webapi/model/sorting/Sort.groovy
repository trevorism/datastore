package com.trevorism.gcloud.webapi.model.sorting

import io.micronaut.core.annotation.Introspected

@Introspected
class Sort {
    String field
    boolean descending
}
