package com.trevorism.gcloud

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

Arbitrary initialArbitrary
Arbitrary createdArbitrary
Arbitrary updatedArbitrary
DatastoreRestClient restClient = new DatastoreRestClient()

Given(~/^a test object is defined$/) { ->
    initialArbitrary = new Arbitrary(date: new Date(), number: 5, decimal: 4.2)
}

Given(~/^the object is created$/) { ->
    createdArbitrary = restClient.store(initialArbitrary)
}

Then(~/^the object can found by listing all objects$/) { ->
    List<Arbitrary> data = restClient.list()
    assert data.find{
        it.id == createdArbitrary.id
    }
}

Then(~/^the object can be retrieved by id$/) { ->
    Arbitrary result = restClient.get(createdArbitrary.id)
    assert result.id == createdArbitrary.id
}

When(~/^the object is updated$/) { ->
    Arbitrary arb = new Arbitrary(number: 22, decimal: 14.6)
    updatedArbitrary = restClient.put(createdArbitrary.id, arb)
}

Then(~/^the object reflects the update$/) { ->
    assert updatedArbitrary.decimal == 14.6d
    assert updatedArbitrary.number == 22
}

When(~/^every object in the list is deleted$/) { ->
    List<Arbitrary> data = restClient.list()
    data.each {
        restClient.delete(it.id)
    }
}

Then(~/^no objects will exist when listing all objects$/) { ->
    assert !restClient.list()
}