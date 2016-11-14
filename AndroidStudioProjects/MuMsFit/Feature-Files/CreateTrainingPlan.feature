#noinspection CucumberUndefinedStep
Feature: Use-Case Create Training Plan

  Scenario: Add a split to the plan
    Given I started the app
    And I pressed on the "addPlan"-Button
    And I typed in the name of the new Plan
    And I pressed on the "okay"-Button
    When I press the "addSplit"-Button
    And I typed in the name of the split
    And I pressed on the "okay"-Button
    Then I see the created split

  Scenario: Add an exercise to the plan
    Given I started the app
    And I pressed on the "addPlan"-Button
    And I typed in the name of the new Plan
    And I pressed on the "okay"-Button
    And I pressed the "addSplit"-Button
    And I typed in the name of split
    And I pressed the "okay"-Button
    When I press the "addExercise"-Button
    And I typed in the name of the exercise
    And I pressed on the "okay"-Button
    Then I see the created exercise

  Scenario: Save the created plan
    Given I started the app
    And I pressed on the "addPlan"-Button
    And I typed in the name of the new Plan
    And I pressed on the "okay"-Button
    When I press the "save"-Button
    And I press the "okay"-Button
    Then I see the created training plan