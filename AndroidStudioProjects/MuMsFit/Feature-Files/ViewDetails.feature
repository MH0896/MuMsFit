Feature: Use-Case View Details

  Scenario: View Details of a chosen training plan
    Given I started the App
    And I am on the "MainActivity" page
    And I already created an training plan
    When I hold a training plan
    And I press the "details"-Button
    Then I see the details of the plan