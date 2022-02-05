package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter
import com.trevorism.gcloud.webapi.model.filtering.FilterConstants
import com.trevorism.gcloud.webapi.model.filtering.SimpleFilter
import org.junit.Test

import java.time.LocalDateTime
import java.time.ZoneOffset

class FilterControllerTest {

    @Test
    void testFilter(){
        FilterController filterController = new FilterController()

        def date = LocalDateTime.of(2022,1,31,23,30,0).atZone(ZoneOffset.systemDefault())
        println date.toInstant().toString()

        def list = filterController.operate(new ComplexFilter(type: FilterConstants.AND,
                simpleFilters: [  new SimpleFilter(type: FilterConstants.TYPE_DATE, field: "date", operator: "<=", value: date.toInstant().toString()) ]
        ), "test")
        list.each {
            println it
        }
    }
}
