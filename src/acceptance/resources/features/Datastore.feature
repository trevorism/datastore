Feature: Crud on an object

  I can perform CRUD and List on an arbitrary object

  Scenario: Create a test object
    Given a test object is defined
    And the object is created
    Then the object can found by listing all objects
    And the object can be retrieved by id

  Scenario: Update a test object
    Given a test object is defined
    And the object is created
    When the object is updated
    Then the object can be retrieved by id
    And the object reflects the update

  Scenario: Delete a test object
     Given a test object is defined
     And the object is created
     When every object in the list is deleted
     Then no objects will exist when listing all objects