package com.trevorism.gcloud.webapi.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.ValueType

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * @author tbrooks
 */
class EntityModule extends SimpleModule{

    EntityModule(){
        addSerializer(Entity.class, new EntitySerializer())
    }

    class EntitySerializer extends JsonSerializer<Entity>{

        @Override
        void serialize(Entity entity, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeStartObject()
            if(entity.key.id)
                gen.writeNumberField("id", entity.key.id)

            entity.getNames().each { k ->
                gen.writeFieldName(k)
                if(entity.getValue(k).getType() == ValueType.TIMESTAMP)
                    gen.writeObject(entity.getValue(k).get().toDate())
                else
                    gen.writeObject(entity.getValue(k).get())
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
