package com.trevorism.gcloud


import com.trevorism.http.headers.HeadersHttpClient
import com.trevorism.http.headers.HeadersJsonHttpClient
import com.trevorism.http.util.ResponseUtils
import com.trevorism.secure.PasswordProvider
import gherkin.deps.com.google.gson.Gson
import gherkin.deps.com.google.gson.GsonBuilder
import gherkin.deps.com.google.gson.reflect.TypeToken

/**
 * @author tbrooks
 */
class DatastoreRestClient {

    HeadersHttpClient client = new HeadersJsonHttpClient()
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()
    PasswordProvider passwordProvider = new PasswordProvider()

    Arbitrary store(def arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = ResponseUtils.getEntity client.post("http://datastore.trevorism.com/api/test",json, ["Authorization":passwordProvider.password])
        gson.fromJson(responseJson, Arbitrary)
    }

    List<Arbitrary> list(){
        String listJson = ResponseUtils.getEntity client.get("http://datastore.trevorism.com/api/test", ["Authorization":passwordProvider.password])
        return gson.fromJson(listJson, new TypeToken<List<Arbitrary>>(){}.getType())
    }

    Arbitrary get(String id){
        String json = ResponseUtils.getEntity client.get("http://datastore.trevorism.com/api/test/${id}", ["Authorization":passwordProvider.password])

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary delete(String id){
        String json = ResponseUtils.getEntity client.delete("http://datastore.trevorism.com/api/test/${id}", ["Authorization":passwordProvider.password])

        return gson.fromJson(json, Arbitrary)
    }

    Arbitrary put(String id, Arbitrary arbitrary){
        String json = gson.toJson(arbitrary)
        String responseJson = ResponseUtils.getEntity client.put("http://datastore.trevorism.com/api/test/${id}",json, ["Authorization":passwordProvider.password])
        return gson.fromJson(responseJson, Arbitrary)
    }
}
