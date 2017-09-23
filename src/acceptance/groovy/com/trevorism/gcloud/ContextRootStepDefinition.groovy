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

Then(~/^the API returns a link to the help page$/) { ->
    assert contextRootContent
    assert contextRootContent.contains("/help")
}

Then(~/^the API returns a link to the help page$/) { ->
    assert pingContent == "pong"
}