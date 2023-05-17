package com.trevorism.gcloud.webapi

import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.ValueType
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.type.Argument
import io.micronaut.serde.Encoder
import io.micronaut.serde.ObjectMapper
import io.micronaut.serde.Serializer
import io.micronaut.serde.exceptions.SerdeException
import jakarta.inject.Inject

@jakarta.inject.Singleton
class EntitySerializer implements Serializer<Entity> {
    @Inject ObjectMapper objectMapper


    @Override
    Serializer<Entity> createSpecific(@NonNull EncoderContext context, @NonNull Argument<? extends Entity> type) throws SerdeException {
        return super.createSpecific(context, type)
    }

    @Override
    void serialize(@NonNull Encoder encoder, @NonNull EncoderContext context, @NonNull Argument<? extends Entity> type, @NonNull Entity value) throws IOException {

        if(value.key.id){
            encoder.encodeKey("id")
            encoder.encodeLong(value.key.id)
        }

        value.getNames().each { k ->
            encoder.encodeKey(k)
            if(value.getValue(k).getType() == ValueType.TIMESTAMP)
                encoder.encodeString(value.getValue(k).get().toString())
            else
                encoder.encodeString(value.getValue(k).get())
        }
        encoder.finishStructure()
    }

    @Override
    boolean isEmpty(@NonNull EncoderContext context, @Nullable Entity value) {
        return super.isEmpty(context, value)
    }

    @Override
    boolean isAbsent(@NonNull EncoderContext context, @Nullable Entity value) {
        return super.isAbsent(context, value)
    }
}
