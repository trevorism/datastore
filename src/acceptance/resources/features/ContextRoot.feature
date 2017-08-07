Feature: Context Root of Datastore
  In order to use the datastore API, it must be available

  Scenario: ContextRoot
    When I navigate to "http://datastore.trevorism.com"
    Then The API returns an array, letting me know where I can go next

  Scenario: ContextRoot on app engine
    When I navigate to "https://trevorism-gcloud.appspot.com"
    Then The API returns an array, letting me know where I can go next

