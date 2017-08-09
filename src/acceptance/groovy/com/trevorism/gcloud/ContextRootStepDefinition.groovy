package com.trevorism.gcloud
this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

def contextRootContent
def pingContent

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