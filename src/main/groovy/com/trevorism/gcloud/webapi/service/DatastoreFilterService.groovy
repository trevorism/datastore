package com.trevorism.gcloud.webapi.service

import com.google.cloud.Timestamp
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.EntityQuery
import com.google.cloud.datastore.StructuredQuery
import com.google.cloud.datastore.StructuredQuery.Filter
import com.google.cloud.datastore.StructuredQuery.PropertyFilter
import com.trevorism.gcloud.webapi.model.filtering.ComplexFilter
import com.trevorism.gcloud.webapi.model.filtering.FilterConstants
import com.trevorism.gcloud.webapi.model.filtering.SimpleFilter

import java.text.DateFormat
import java.text.SimpleDateFormat

class DatastoreFilterService implements FilterService{

    private Datastore datastore
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @Override
    def filter(ComplexFilter request, String kind) {
        def queryBuilder = EntityQuery.Builder.newInstance().setKind(kind)

        if(request?.simpleFilters || request?.complexFilters)
        {
            Filter filter = createQueryFilter(request, kind)
            queryBuilder.setFilter(filter)
        }

        def query = queryBuilder.build()
        def results = getDatastore().run(query)
        new EntityList(results).toList()

    }

    private Datastore getDatastore() {
        if (!datastore)
            datastore = DatastoreOptions.getDefaultInstance().getService()
        return datastore
    }

    private Filter createQueryFilter(ComplexFilter complexFilter, String kind) {
        if(complexFilter.type == FilterConstants.AND){
            return handleConjunction(complexFilter, kind)
        }

        if(complexFilter.type == FilterConstants.OR){
            throw new RuntimeException("OR queries are not supported")
        }

        if (complexFilter.simpleFilters) {
            return createSimpleFilter(complexFilter.simpleFilters[0], kind)
        }

    }

    private Filter handleConjunction(ComplexFilter complexFilter, String kind) {
        def list = []
        complexFilter.simpleFilters.each {
            list << createSimpleFilter(it, kind)
        }

        return StructuredQuery.CompositeFilter.and(*list)
    }

    Filter createSimpleFilter(SimpleFilter filter, String kind) {
        def value = filter.value
        if(filter.field.toLowerCase() == "id"){
            filter.field = "__key__"
            value = getDatastore().newKeyFactory().setKind(kind).newKey(Long.valueOf(value))
        }
        else if(filter.type?.toLowerCase() == FilterConstants.TYPE_BOOLEAN)
            value = Boolean.valueOf(value)
        else if(filter.type?.toLowerCase() == FilterConstants.TYPE_DATE)
            value = Timestamp.of(dateFormat.parse(value))
        else if(filter.type?.toLowerCase() == FilterConstants.TYPE_NUMBER)
            value = Double.valueOf(value)


        switch (filter.operator) {
            case FilterConstants.OPERATOR_EQUAL: return PropertyFilter.eq(filter.field.toLowerCase(), value)
            case FilterConstants.OPERATOR_GREATER_THAN: return PropertyFilter.gt(filter.field.toLowerCase(), value)
            case FilterConstants.OPERATOR_GREATER_THAN_OR_EQUAL: return PropertyFilter.ge(filter.field.toLowerCase(), value)
            case FilterConstants.OPERATOR_LESS_THAN: return PropertyFilter.lt(filter.field.toLowerCase(), value)
            case FilterConstants.OPERATOR_LESS_THAN_OR_EQUAL: return PropertyFilter.le(filter.field.toLowerCase(), value)
            case FilterConstants.OPERATOR_NOT_EQUAL: throw new RuntimeException("Unsupported operator")
        }
    }
}
