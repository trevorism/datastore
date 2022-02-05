package com.trevorism.gcloud.webapi.model.filtering


class ComplexFilter {
    String type // simple | and | or   (simple is the default)
    List<SimpleFilter> simpleFilters = []
    List<ComplexFilter> complexFilters = []
}
