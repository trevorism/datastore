package com.trevorism.gcloud

import com.trevorism.http.HttpClient
import com.trevorism.http.JsonHttpClient
import gherkin.deps.com.google.gson.Gson
import gherkin.deps.com.google.gson.reflect.TypeToken


/**
 * @author tbrooks
 */
class DatastoreRestClient {

    HttpClient client = new JsonHttpClient()
    Gson gson = new Gson()

    Arbitrary store(def arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = client.post("http://datastore.trevorism.com/api/test",json)
        gson.fromJson(responseJson, Arbitrary)
    }

    List<Arbitrary> list(){
        String listJson = client.get("http://datastore.trevorism.com/api/test")
        return gson.fromJson(listJson, new TypeToken<List<Arbitrary>>(){}.getType())
    }

    Arbitrary get(String id){
        String json = client.get("http://datastore.trevorism.com/api/test/${id}")

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary delete(String id){
        String json = client.delete("http://datastore.trevorism.com/api/test/${id}")

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary put(String id, Arbitrary arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = client.put("http://datastore.trevorism.com/api/test/${id}",json)
        return gson.fromJson(responseJson, Arbitrary)
    }
}
