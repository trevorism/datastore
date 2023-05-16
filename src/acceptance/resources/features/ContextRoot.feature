Feature: Context Root of Datastore
  In order to use the datastore API, it must be available

  Scenario: ContextRoot https
    Given the datastore application is alive
    When I navigate to "https://datastore.data.trevorism.com"
    Then the API returns a link to the help page

  Scenario: Ping https
    Given the datastore application is alive
    When I navigate to /ping on "https://datastore.data.trevorism.com"
    Then pong is returned, to indicate the service is alive

  Scenario: Ping on app engine
    Given the datastore application is alive
    When I navigate to /ping on "https://datastore.data.trevorism.com"
    Then pong is returned, to indicate the service is alive
