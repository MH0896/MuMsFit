Feature: Use-Case Create Training Plan

  Scenario: As a user I can create a training plan
    Given I wait for "MainActivity" screen to appear
    Then I press the "fab"
    And I enter "myTestPlan" into input field "input"
    And I press "PositiveButton"
    And I press "readyButton"
    And I press "PositiveButton"
    Then I should see the new plan "myTestPlan" in the listView "ViewPlan"