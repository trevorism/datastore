Feature: Crud on an object

  I can perform CRUD and List on an arbitrary object

  Scenario: Create a test object
    Given the datastore application is alive
    And a test object is defined
    And the object is created
    Then the object can found by listing all objects
    And the object can be retrieved by id

  Scenario: Update a test object
    Given the datastore application is alive
    And a test object is defined
    And the object is created
    When the object is updated
    Then the object can be retrieved by id
    And the object reflects the update

  Scenario: Delete a test object
     Given the datastore application is alive
     And a test object is defined
     And the object is created
     When every object in the list is deleted
     Then no objects will exist when listing all objects

  Scenario: An invalid object throws an error
    Given the datastore application is alive
    And an invalid test object is defined
    When the invalid test object is created
    Then an error is thrown, indicating the failure