package com.trevorism.gcloud.webapi.service

import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter

interface FilterService {

    def filter(ComplexFilter request, String kind)

}