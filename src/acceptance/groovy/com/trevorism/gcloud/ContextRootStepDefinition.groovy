package com.trevorism.gcloud
this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

def contextRootContent
def pingContent

Given(~/^the datastore application is alive$/) { ->
    try{
        new URL("http://datastore.trevorism.com/ping").text
    }
    catch (Exception ignored){
        Thread.sleep(10000)
        new URL("http://datastore.trevorism.com/ping").text
    }
}

When(~/^I navigate to "([^"]*)"$/) { String url ->
    contextRootContent = new URL(url).text
}

When(~/^I navigate to \/ping on "([^"]*)"$/) { String url ->
    pingContent = new URL("${url}/ping").text
}

Then(~/^the API returns an array, letting me know where I can go next$/) { ->
    assert contextRootContent
    assert contextRootContent == '["ping","help","api"]'
}

Then(~/^pong is returned, to indicate the service is alive$/) { ->
    assert pingContent == "pong"
}