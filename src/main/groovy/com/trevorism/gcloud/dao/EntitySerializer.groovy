package com.trevorism.gcloud.dao

import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.ValueType

class EntitySerializer {

    static Map<String, Object> serialize(Entity entity) {
        Map<String, Object> map = [:]
        if (entity.key.id) {
            map.put("id", entity.key.id)
        }

        entity.getNames().each { name ->
            if (entity.getValue(name).getType() == ValueType.TIMESTAMP)
                map.put(name, DateFormatProvider.dateFormat.format(entity.getValue(name).get().toDate()))
            else
                map.put(name, entity.getValue(name).get())
        }
        return map
    }

}
