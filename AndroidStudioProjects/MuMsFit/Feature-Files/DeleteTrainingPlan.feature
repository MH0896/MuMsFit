#noinspection CucumberUndefinedStep
Feature: Use-Case Delete Trainings Plan

  Scenario: Delete one training plan
    Given I started the App
    And I am on the "MainActivity" page
    And I already created an training plan
    When I hold a training plan
    And I press the "delete"-Button
    Then I see that the chosen plan was deleted

  Scenario: Delete all training plans
    Given I started the App
    And I am on the "MainActivity" page
    And I already created an training plan
    When I hold a training plan
    And I press the "selectAll"-Button
    And I press the "delete"-Button
    Then I see that the chosen plan was deleted