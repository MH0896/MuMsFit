Feature: View the Details of a training plan

  Scenario: As a valid user I can view the deatails of a created training plan
    When I press "addPlan"
    And I enter text "testPlanVD" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanVD"
    And I long press "testPlanVD"
    And I press "item_details"
    Then I see "Anzahl der Einheiten: 0"
    And I see "Erstellt am:"
    And I see "Zuletzt durchgeführt am:"

  Scenario: As a valid user I can not view the details of several plans at the same time
    When I press "addPlan"
    And I enter text "testPlanVD1" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanVD1"
    And I long press "testPlanVD1"
    And I press "testPlanVD"
    Then I press "item_details"