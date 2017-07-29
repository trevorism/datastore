package com.trevorism.gcloud.dao

import com.google.appengine.api.datastore.Entity

/**
 * @author tbrooks
 */
interface DatastoreDAO {

    Entity create(Map<String, Object> data)

    Entity read(long id)
    List<Entity> readAll()

    Entity update(long id, Map<String, Object>  data)

    Entity delete(long id)
}
