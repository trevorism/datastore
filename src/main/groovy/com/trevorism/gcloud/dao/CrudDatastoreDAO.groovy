package com.trevorism.gcloud.dao

import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.gcloud.webapi.service.EntityList
import io.micronaut.http.HttpRequest
import io.micronaut.runtime.http.scope.RequestAware
import io.micronaut.runtime.http.scope.RequestScope
import jakarta.inject.Inject

import java.text.SimpleDateFormat
import java.util.logging.Logger

@RequestScope
class CrudDatastoreDAO implements DatastoreDAO, RequestAware {


    private static final Logger log = Logger.getLogger(CrudDatastoreDAO.class.name)
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()

    @Inject
    EntitySerializer entitySerializer
    @Inject
    DateFormatProvider dateFormatProvider


    private Datastore datastore
    private String tenant
    private String kind

    @Override
    Map<String, Object> create(Map<String, Object> data) {
        validate(data)

        def toBeCreated = setEntityProperties(data)
        Entity entity = getDatastore().put(toBeCreated)
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
    Map<String, Object> read(long id) {
        Key key = getDatastore().newKeyFactory().setKind(kind).newKey(id)
        try {
            return EntitySerializer.serialize(getDatastore().get(key))
        } catch (Exception ignored) {
            log.fine("Unable to get entity with id: $id")
            return null
        }
    }

    @Override
    List<Map<String, Object>> readAll() {
        EntityQuery query = EntityQuery.Builder.newInstance().setKind(kind).build()
        def results = getDatastore().run(query)
        new EntityList(results).toList().collect {
            entitySerializer.serialize(it)
        }
    }

    @Override
    Map<String, Object> update(long id, Map<String, Object> data) {
        Map<String, Object> entityExists = read(id)

        if (!entityExists)
            return null

        data.put("id", id)
        Key key = getDatastore().newKeyFactory().setKind(kind).newKey(id)

        FullEntity<IncompleteKey> toBeUpdated = setEntityProperties(key, data)
        return entitySerializer.serialize(getDatastore().put(toBeUpdated))
    }

    @Override
    Map<String, Object> delete(long id) {
        Key key = getDatastore().newKeyFactory().setKind(kind).newKey(id)
        Map<String, Object> entity = read(id)
        if (!entity)
            return null

        getDatastore().delete(key)
        return entity
    }

    private FullEntity<IncompleteKey> setEntityProperties(Map<String, Object> data) {
        def key = createEntityKey(data)
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

    private IncompleteKey createEntityKey(Map<String, Object> data) {
        long id = getIdFromObject(data)
        KeyFactory keyFactory = getDatastore().newKeyFactory().setKind(kind)

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

    Datastore getDatastore() {
        if (!datastore) {
            if(tenant){
                datastore = DatastoreOptions.newBuilder().setNamespace(tenant).build().getService()
            }
            else{
                datastore = DatastoreOptions.getDefaultInstance().getService()
            }
        }
        return datastore
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

    @Override
    void setRequest(HttpRequest<?> request) {
        Optional<String> wrappedTenant = request.getAttribute("tenant", String)
        if(wrappedTenant.isPresent())
            tenant = wrappedTenant.get()
    }

    void setKind(String kind){
        this.kind = kind
    }
}