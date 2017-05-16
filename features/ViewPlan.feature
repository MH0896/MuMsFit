Feature: View a created tetraining plan

  Scenario: As a valid user I can view a created training plan
    When I press "addPlan"
    And I enter text "ViewTestPlan" into field with id "calabash"
    And I press the "Okay" button
    And I press the "Trainingstag hinzufügen" button
    And I enter text "View Tag 1" into field with id "calabash"
    And I see "View Tag 1"
    And I press the "Okay" button
    And I press "Übung hinzufügen"
    And I enter text "View Übung 1" into field with id "c_name"
    And I enter text "8 mal" into field with id "c_reps"
    And I enter text "42" into field with id "c_sw"
    And I press the "Okay" button
    And I see "Übung 1 Reps: 8 mal Startgewicht: 42"
    And I press "readyButton"
    And I press the "Okay" button
    And I see "ViewTestPlan"
    Then I press "ViewTestPlan"
    And I see "View Tag 1"
    And I see "View Übung 1"
    And I see "Übung 1 Reps: 8 mal Startgewicht: 42"

  Scenario: As a valid user I can go back to the start screen while viewing
    When I press "addPlan"
    And I enter text "ViewTestPlan2" into field with id "calabash"
    And I press the "Okay" button
    And I press the "Trainingstag hinzufügen" button
    And I enter text "View Tag 2" into field with id "calabash"
    And I see "View Tag 1"
    And I press the "Okay" button
    And I press "Übung hinzufügen"
    And I enter text "View Übung 2" into field with id "c_name"
    And I enter text "8 mal" into field with id "c_reps"
    And I enter text "42" into field with id "c_sw"
    And I press the "Okay" button
    And I see "Übung 1 Reps: 8 mal Startgewicht: 42"
    And I press "readyButton"
    And I press the "Okay" button
    And I see "ViewTestPlan2"
    Then I press "ViewTestPlan2"
    And I see "View Tag 2"
    And I see "View Übung 2"
    And I see "Übung 1 Reps: 8 mal Startgewicht: 42"
    And I wait for 1 second
    And I go back
    And I see "ViewTestPlan2"