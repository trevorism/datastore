package com.trevorism.gcloud.webapi.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.appengine.api.datastore.Entity

/**
 * @author tbrooks
 */
class EntityModule extends SimpleModule{

    public EntityModule(){
        addSerializer(Entity.class, new EntitySerializer())
    }

    class EntitySerializer extends JsonSerializer<Entity>{

        @Override
        void serialize(Entity value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeStartObject()
            if(!value.properties.containsKey("id"))
                gen.writeNumberField("id", value.key.id)
            value.properties.each { k,v ->
                gen.writeFieldName(k)
                gen.writeObject(v)

            }
            gen.writeEndObject()
        }

        @Override
        Class<Entity> handledType()
        {
            return Entity.class
        }

    }
}
