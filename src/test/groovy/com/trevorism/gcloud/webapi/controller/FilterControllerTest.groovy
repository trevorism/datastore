package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter
import com.trevorism.gcloud.webapi.model.filtering.FilterConstants
import com.trevorism.gcloud.webapi.model.filtering.SimpleFilter
import com.trevorism.gcloud.webapi.service.FilterService
import org.junit.jupiter.api.Test

import java.time.LocalDateTime
import java.time.ZoneOffset

class FilterControllerTest {

    @Test
    void testFilter() {
        FilterController filterController = new FilterController()
        filterController.filterService = { it, k -> [1, 2] } as FilterService
        def date = LocalDateTime.of(2022, 1, 31, 23, 30, 0).atZone(ZoneOffset.systemDefault())

        def list = filterController.operate("test", new ComplexFilter(type: FilterConstants.AND,
                simpleFilters: [new SimpleFilter(type: FilterConstants.TYPE_DATE, field: "date", operator: "<=", value: date.toInstant().toString())]
        ))
        assert list
    }
}
