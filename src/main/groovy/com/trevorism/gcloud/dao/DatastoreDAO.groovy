package com.trevorism.gcloud.dao

import com.google.cloud.datastore.Datastore

/**
 * @author tbrooks
 */
interface DatastoreDAO {

    Map<String, Object> create(String kind, Map<String, Object> data)

    Map<String, Object> read(String kind, long id)
    List<Map<String, Object>> readAll(String kind)

    Map<String, Object> update(String kind, long id, Map<String, Object>  data)

    Map<String, Object> delete(String kind, long id)

}
