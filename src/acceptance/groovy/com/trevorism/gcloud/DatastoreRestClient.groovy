package com.trevorism.gcloud

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient


/**
 * @author tbrooks
 */
class DatastoreRestClient {

    SecureHttpClient client = new DefaultSecureHttpClient()
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()


    def attemptToStoreInvalid(def invalid){
        String json = gson.toJson(invalid)
        String responseJson = client.post("https://datastore.trevorism.com/api/test",json)
        return responseJson
    }

    Arbitrary store(def arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = client.post("https://datastore.trevorism.com/api/test",json)
        gson.fromJson(responseJson, Arbitrary)
    }

    List<Arbitrary> list(){
        String listJson = client.get("https://datastore.trevorism.com/api/test")
        return gson.fromJson(listJson, new TypeToken<List<Arbitrary>>(){}.getType())
    }

    Arbitrary get(String id){
        String json = client.get("https://datastore.trevorism.com/api/test/${id}")

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary delete(String id){
        String json = client.delete("https://datastore.trevorism.com/api/test/${id}")

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary put(String id, Arbitrary arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = client.put("https://datastore.trevorism.com/api/test/${id}",json)
        return gson.fromJson(responseJson, Arbitrary)
    }
}
