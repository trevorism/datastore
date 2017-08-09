Feature: Context Root of Datastore
  In order to use the datastore API, it must be available

  Scenario: ContextRoot
    When I navigate to "http://datastore.trevorism.com"
    Then the API returns an array, letting me know where I can go next

  Scenario: ContextRoot on app engine
    When I navigate to "https://trevorism-gcloud.appspot.com"
    Then the API returns an array, letting me know where I can go next

  Scenario: Ping
    When I navigate to /ping on "http://datastore.trevorism.com"
    Then pong is returned, to indicate the service is alive

  Scenario: Ping on app engine
    When I navigate to /ping on "https://trevorism-gcloud.appspot.com"
    Then pong is returned, to indicate the service is alive
