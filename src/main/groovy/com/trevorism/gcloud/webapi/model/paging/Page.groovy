package com.trevorism.gcloud.webapi.model.paging

import io.micronaut.core.annotation.Introspected

@Introspected
class Page {
    int page
    int pageSize
    int limit
}
