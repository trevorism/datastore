package com.trevorism.gcloud.webapi.model.filtering

import io.micronaut.core.annotation.Introspected

@Introspected
class ComplexFilter {
    String type // simple | and | or   (simple is the default)
    List<SimpleFilter> simpleFilters = []
    List<ComplexFilter> complexFilters = []
}
