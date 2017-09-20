package com.trevorism.gcloud.dao

import com.google.appengine.api.datastore.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder


/**
 * @author tbrooks
 */
class CrudDatastoreDAO implements DatastoreDAO {

    private final DatastoreService datastore
    private final String kind
    private final Gson gson

    CrudDatastoreDAO(String kind){
        this.kind = kind.toLowerCase()
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        datastore = DatastoreServiceFactory.getDatastoreService()
    }

    @Override
    Entity create(Map<String, Object> data) {
        validateId(data)

        Entity entity = setEntityFromJSONObject(kind, data)
        Key key = datastore.put(entity)
        return createReturnEntity(key, entity)
    }

    private static void validateId(Map<String, Object> jsonObject) {
        if(!jsonObject["id"])
            return

        def id = jsonObject["id"]
        try{
            Long.parseLong(id.toString())
        }catch(Exception e){
            throw new RuntimeException("Invalid ID. ID must be a number: ${id}", e)
        }
    }

    @Override
    Entity read(long id) {
        Key key = KeyFactory.createKey(kind, id)
        try{
            return datastore.get(key)
        }catch(EntityNotFoundException ignored){
            return null
        }
    }

    @Override
    List<Entity> readAll() {
        Query query = new Query(kind)
        PreparedQuery resultSet = datastore.prepare(query);
        return resultSet.asList(FetchOptions.Builder.withDefaults())
    }

    @Override
    Entity update(long id, Map<String, Object> data) {
        Entity entityExists = read(id)

        if(!entityExists)
            return null

        data.put("id", id)
        Entity entity = setEntityProperties(entityExists, data)
        Key key = datastore.put(entity)
        return createReturnEntity(key, entity)
    }

    @Override
    Entity delete(long id) {
        Key key = KeyFactory.createKey(kind, id)
        Entity entity = read(id)
        if(!entity)
            return null
        datastore.delete(key)
        return entity
    }

    private Entity setEntityFromJSONObject(String kind, Map<String, Object> data) {
        Entity entity = createEmptyEntity(kind, data)
        setEntityProperties(entity, data)
    }

    private Entity setEntityProperties(Entity entity, Map<String, Object> data) {
        data.each { k, v ->
            if(v instanceof List || v instanceof Map){
                v = gson.toJson(v)
            }
            entity.setIndexedProperty(k.toLowerCase(), v)
        }
        return entity
    }

    private static Entity createReturnEntity(Key key, Entity entity) {
        Entity returnEntity = new Entity(key)
        returnEntity.setPropertiesFrom(entity)
        return returnEntity
    }

    private static Entity createEmptyEntity(String kind, Map<String, Object> data) {
        long id = getIdFromObject(data)

        if (id != 0)
            return new Entity(kind, id)
        else
            return new Entity(kind)

    }

    private static long getIdFromObject(Map<String, Object> jsonObject) {
        try{
            if(jsonObject.containsKey("id"))
                return Long.valueOf(jsonObject.get("id").toString())
        }catch (Exception ignored){ }
        return 0
    }
}