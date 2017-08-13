package com.trevorism.gcloud.webapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.appengine.api.datastore.Entity
import com.trevorism.gcloud.LocalAppEngineTestBase
import com.trevorism.gcloud.webapi.serialize.JacksonConfig
import org.junit.Test

/**
 * @author tbrooks
 */
class JacksonConfigTest extends LocalAppEngineTestBase{

    @Test
    void testEntitySerialization(){
        JacksonConfig jacksonConfig = new JacksonConfig()
        ObjectMapper mapper = jacksonConfig.getContext(null)

        Entity entity = new Entity("kind", 2)
        entity.setIndexedProperty("name", "trevor")
        entity.setIndexedProperty("date", new GregorianCalendar(2016,3,17,12,00,50).getTime())

        assert "{\"id\":2,\"date\":\"2016-04-17T12:00:50Z\",\"name\":\"trevor\"}" == mapper.writeValueAsString(entity)

    }
}
