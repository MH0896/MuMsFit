Feature: Edit a created training plan

  Scenario: As a valid user I can rename an existing training plan
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
    And I enter text "42kg" into field with id "c_sw"
    And I press the "Okay" button
    And I see "Übung 1 Reps: 8 mal Startgewicht: 42kg"
    And I press "readyButton"
    And I press the "Okay" button
    And I see "ViewTestPlan"
    And I long press "ViewTestPlan"
    And I press "item_menu_overflow"
    And I press "Edit"
    And I clear input field with id "calabash"
    And I enter text "EditTestPlan" into field with id "calabash"
    And I press the "Okay" button
    Then I see "EditTestPlan"

  Scenario: As a valid user I can add exercises to a training plan
    When I press "EditTestPlan"
    And I press "item_edit_menu"
    And I press "Übung hinzufügen"
    And I clear input field with id "c_name"
    And I clear input field with id "c_reps"
    And I clear input field with id "c_sw"
    And I clear input field with id "c_split"
    And I enter text "Edit Übung 1" into field with id "c_name"
    And I enter text "87 mal" into field with id "c_reps"
    And I enter text "47kg" into field with id "c_sw"
    And I enter text "EditSplit" into field with id "c_split"
    And I see "EditSplit"
    And I press the "Okay" button
    And I see "Edit Übung 1 Reps: 87 mal Startgewicht: 47kg"
    Then I go back
    And I see "EditSplit"
    And I see "Edit Übung 1 Reps: 87 mal Startgewicht: 47kg"

