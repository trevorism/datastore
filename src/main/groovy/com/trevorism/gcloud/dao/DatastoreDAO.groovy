package com.trevorism.gcloud.dao

import com.google.cloud.datastore.Datastore

/**
 * @author tbrooks
 */
interface DatastoreDAO {

    Map<String, Object> create(Map<String, Object> data)

    Map<String, Object> read(long id)
    List<Map<String, Object>> readAll()

    Map<String, Object> update(long id, Map<String, Object>  data)

    Map<String, Object> delete(long id)

    Datastore getDatastore()
}
