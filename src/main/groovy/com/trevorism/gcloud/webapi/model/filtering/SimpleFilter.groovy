package com.trevorism.gcloud.webapi.model.filtering

import groovy.transform.ToString

@ToString
class SimpleFilter {
    String type // string | number | date   (string is the default)
    String field
    String operator
    String value
}
