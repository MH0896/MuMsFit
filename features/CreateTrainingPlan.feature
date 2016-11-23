Feature: Create Training Plan

  Scenario: As a valid user I can create a blank training plan
    When I press "addPlan"
    And I enter text "testPlan" into field with id "calabash"
    And I press the "Okay" button
    And I press "readyButton"
    And I press the "Okay" button
    Then I see "testPlan"

  Scenario: As a valid user I must give a name to a new training plan
    When I press "addPlan"
    And I enter text "" into field with id "calabash"
    And I press the "Okay" button
    Then I see "Bitte einen Namen eingeben"

  Scenario: As a valid user I can't name a training plan with a name another plan has
    When I press "addPlan"
    And I enter text "testPlan" into field with id "calabash"
    And I press the "Okay" button
    Then I see "Name schon vergeben. Bitte wählen Sie einen anderen!"

  Scenario: As a valid User I can create and save a plan with a split and an exercise
    When I press "addPlan"
    And I enter text "testPlan2" into field with id "calabash"
    And I press the "Okay" button
    And I press the "Trainingstag hinzufügen" button
    And I enter text "Tag 1" into field with id "calabash"
    And I see "Tag 1"
    And I press the "Okay" button
    And I press "Übung hinzufügen"
    And I enter text "Übung 1" into field with id "c_name"
    And I enter text "8 mal" into field with id "c_reps"
    And I enter text "42kg" into field with id "c_sw"
    And I press the "Okay" button
    And I see "Übung 1 Reps: 8 mal Startgewicht: 42kg"
    And I press "readyButton"
    And I press the "Okay" button
    Then I see "testPlan2"

  Scenario: As a valid user I can cancel creating a training plan at the beginning
    When I press "addPlan"
    And I enter text "testPlan3" into field with id "calabash"
    And I press the "Abbrechen" button
    Then I don't see "testPlan3"

  Scenario: As a valid user I can cancel creating a training plan while adding exercises
    When I press "addPlan"
    And I enter text "testPlan3" into field with id "calabash"
    And I press the "Okay" button
    And I wait for 1 second
    And I go back
    And I press the "Ja" button
    Then I don't see "testPlan3"
