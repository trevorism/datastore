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

    public static final String BASE_URL = "https://datastore.data.trevorism.com"

    SecureHttpClient client = new DefaultSecureHttpClient()
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()


    def attemptToStoreInvalid(def invalid){
        String json = gson.toJson(invalid)
        String responseJson = client.post("${BASE_URL}/object/test",json)
        return responseJson
    }

    Arbitrary store(def arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = client.post("${BASE_URL}/object/test",json)
        gson.fromJson(responseJson, Arbitrary)
    }

    List<Arbitrary> list(){
        String listJson = client.get("${BASE_URL}/object/test")
        return gson.fromJson(listJson, new TypeToken<List<Arbitrary>>(){}.getType())
    }

    Arbitrary get(String id){
        String json = client.get("${BASE_URL}/object/test/${id}")

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary delete(String id){
        String json = client.delete("${BASE_URL}/object/test/${id}")

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary put(String id, Arbitrary arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = client.put("${BASE_URL}/object/test/${id}",json)
        return gson.fromJson(responseJson, Arbitrary)
    }
}
