package com.trevorism.gcloud.webapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.Timestamp
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.IncompleteKey
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.PathElement
import com.google.common.collect.ImmutableList
import com.trevorism.gcloud.webapi.serialize.JacksonConfig
import org.junit.Test

/**
 * @author tbrooks
 */
class JacksonConfigTest {

    @Test
    void testEntitySerialization(){
        JacksonConfig jacksonConfig = new JacksonConfig()
        ObjectMapper mapper = jacksonConfig.getContext(null)

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        def calendar = new GregorianCalendar(2016,3,17,12,00,50)
        calendar.setTimeZone(timeZone)

        def builder = Entity.newBuilder(new Key("trevorism-gcloud","",ImmutableList.of(new PathElement("kind",null, 2))))
        builder.set("name", "trevor")
        builder.set("date", Timestamp.of(calendar.getTime()))

        def entity = builder.build()
        println mapper.writeValueAsString(entity)
        assert "{\"id\":2,\"date\":\"2016-04-17T12:00:50Z\",\"name\":\"trevor\"}" == mapper.writeValueAsString(entity)

    }
}
