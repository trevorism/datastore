package com.trevorism.gcloud
/**
 * @author tbrooks
 */

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

def content

When(~/^I navigate to "([^"]*)"$/) { String url ->
    content = new URL(url).text
}

Then(~/^The API returns an array, letting me know where I can go next$/) { ->
    assert content
    assert content == '["ping","help","api"]'
}