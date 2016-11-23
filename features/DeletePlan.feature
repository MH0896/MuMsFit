Feature: Delete one or more Training Plans

  Scenario: As a valid user I can delete one training plan
    When I press "addPlan"
    And I enter text "testPlanD" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanD"
    And I long press "testplan"
    And I press "item_delete"
    And I press the "Okay" button
    Then I don't see "testPlanD"

  Scenario: As a valid user I can delete several training plans
    When I press "addPlan"
    And I enter text "testPlanD1" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanD1"
    And I press "addPlan"
    And I enter text "testPlanD2" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanD2"
    And I press "addPlan"
    And I enter text "testPlanD3" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanD3"
    And I long press "testPlanD2"
    And I press "testPlanD1"
    And I press "item_delete"
    And I press the "Okay" button
    Then I don't see "testPlanD1"
    And I don't see "testPlanD2"

  Scenario: As a valid user I can delete all training plans
    When I press "addPlan"
    And I enter text "testPlanD1" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanD1"
    And I press "addPlan"
    And I enter text "testPlanD2" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    And I see "testPlanD2"
    And I long press "testPlanD3"
    And I press "item_select_all"
    And I press "item_delete"
    And I press the "Okay" button
    Then I don't see "testPlanD1"
    And I don't see "testPlanD2"
    And I don't see "testPlanD3"