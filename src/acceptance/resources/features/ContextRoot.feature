Feature: Context Root of Datastore
  In order to use the datastore API, it must be available

  Scenario: ContextRoot https
    Given the datastore application is alive
    When I navigate to "https://datastore.trevorism.com"
    Then the API returns a link to the help page

  Scenario: ContextRoot on app engine
    Given the datastore application is alive
    When I navigate to "https://trevorism-gcloud.appspot.com"
    Then the API returns a link to the help page

  Scenario: Ping https
    Given the datastore application is alive
    When I navigate to /ping on "https://datastore.trevorism.com"
    Then pong is returned, to indicate the service is alive

  Scenario: Ping on app engine
    Given the datastore application is alive
    When I navigate to /ping on "https://trevorism-gcloud.appspot.com"
    Then pong is returned, to indicate the service is alive
