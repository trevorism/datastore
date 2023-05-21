package com.trevorism.gcloud.webapi.service

import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.gcloud.bean.DatastoreProvider
import com.trevorism.gcloud.bean.DateFormatProvider
import com.trevorism.gcloud.bean.EntitySerializer
import jakarta.inject.Inject

import java.util.logging.Logger

@jakarta.inject.Singleton
class CrudDatastoreRepository implements DatastoreRepository {

    private static final Logger log = Logger.getLogger(CrudDatastoreRepository.class.name)
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()

    @Inject
    EntitySerializer entitySerializer
    @Inject
    DateFormatProvider dateFormatProvider
    @Inject
    DatastoreProvider datastoreProvider

    @Override
    Map<String, Object> create(String kind, Map<String, Object> data) {
        validate(data)
        def toBeCreated = setEntityProperties(kind, data)
        Entity entity = datastoreProvider.getDatastore().put(toBeCreated)
        entitySerializer.serialize(entity)
    }

    private static void validate(Map<String, Object> jsonObject) {
        if (jsonObject.containsKey("key"))
            throw new RuntimeException("Invalid object definition. Object cannot have a 'key' column")

        if (!jsonObject["id"])
            return

        def id = jsonObject["id"]
        try {
            Long.parseLong(id.toString())
        } catch (Exception e) {
            throw new RuntimeException("Invalid ID. ID must be a number instead of: ${id}", e)
        }
    }

    @Override
    Map<String, Object> read(String kind, long id) {
        Key key = datastoreProvider.getDatastore().newKeyFactory().setKind(kind).newKey(id)
        try {
            return entitySerializer.serialize(datastoreProvider.getDatastore().get(key))
        } catch (Exception ignored) {
            log.fine("Unable to get entity with id: $id")
            return null
        }
    }

    @Override
    List<Map<String, Object>> readAll(String kind) {
        EntityQuery query = EntityQuery.newEntityQueryBuilder().setKind(kind).build()
        def results = datastoreProvider.getDatastore().run(query)
        new EntityList(results).toList().collect {
            entitySerializer.serialize(it)
        }
    }

    @Override
    Map<String, Object> update(String kind, long id, Map<String, Object> data) {
        Map<String, Object> entityExists = read(kind, id)

        if (!entityExists)
            return null

        data.put("id", id)
        Key key = datastoreProvider.getDatastore().newKeyFactory().setKind(kind).newKey(id)

        FullEntity<IncompleteKey> toBeUpdated = setEntityProperties(key, data)
        return entitySerializer.serialize(datastoreProvider.getDatastore().put(toBeUpdated))
    }

    @Override
    Map<String, Object> delete(String kind, long id) {
        Key key = datastoreProvider.getDatastore().newKeyFactory().setKind(kind).newKey(id)
        Map<String, Object> entity = read(kind, id)
        if (!entity)
            return null

        datastoreProvider.getDatastore().delete(key)
        return entity
    }

    private FullEntity<IncompleteKey> setEntityProperties(String kind, Map<String, Object> data) {
        def key = createEntityKey(kind, data)
        setEntityProperties(key, data)
    }

    private FullEntity<IncompleteKey> setEntityProperties(IncompleteKey key, Map<String, Object> data) {
        def builder = FullEntity.newBuilder(key)
        data.each { k, v ->
            if (v instanceof List || v instanceof Map) {
                v = gson.toJson(v)
            }
            if (isParseableToDate(v)) {
                Date date = parseDate(v)
                v = Timestamp.of(date)
            }
            builder.set(k.toLowerCase(), v)
        }
        return builder.build()
    }

    private IncompleteKey createEntityKey(String kind, Map<String, Object> data) {
        long id = getIdFromObject(data)
        KeyFactory keyFactory = datastoreProvider.getDatastore().newKeyFactory().setKind(kind)

        if (id != 0)
            return keyFactory.newKey(id)
        else
            return keyFactory.newKey()
    }

    private static long getIdFromObject(Map<String, Object> data) {
        try {
            if (data.containsKey("id"))
                return Long.valueOf(data.get("id").toString())
        } catch (Exception ignored) {
        }
        return 0
    }

    private boolean isParseableToDate(Object o) {
        try {
            dateFormatProvider.dateFormat.parse(o.toString())
            return true
        } catch (Exception ignored) {
            return false
        }
    }

    private Date parseDate(Object o) {
        return dateFormatProvider.dateFormat.parse(o.toString())
    }

}