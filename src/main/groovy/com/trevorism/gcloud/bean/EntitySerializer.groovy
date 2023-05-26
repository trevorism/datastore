package com.trevorism.gcloud.bean

import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.ValueType
import jakarta.inject.Inject

@jakarta.inject.Singleton
class EntitySerializer {

    @Inject
    DateFormatProvider dataFormatProvider

    Map<String, Object> serialize(Entity entity) {
        Map<String, Object> map = [:]
        if (entity.key.id) {
            map.put("id", entity.key.id)
        }

        entity.getNames().each { name ->
            if (entity.getValue(name).getType() == ValueType.TIMESTAMP)
                map.put(name, dataFormatProvider.dateFormat.format(entity.getValue(name).get().toDate()))
            else
                map.put(name, entity.getValue(name).get())
        }
        return map
    }

}
