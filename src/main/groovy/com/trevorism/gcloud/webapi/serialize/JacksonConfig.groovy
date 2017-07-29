package com.trevorism.gcloud.webapi.serialize

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * @author tbrooks
 */
@Provider
class JacksonConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper

    public JacksonConfig() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        objectMapper.setDateFormat(dateFormat)
        objectMapper.registerModule(new EntityModule())
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper
    }
}