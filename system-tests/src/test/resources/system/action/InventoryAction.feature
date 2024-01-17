@Ignore
Feature: Inventory reusable scenarios

  @GetInventory
  Scenario: Verify stock
    Given url inventory_url
    And path '/inventory/' + articleId
    When method get
    Then status 200

  @Replenish
  Scenario: Emit stock event
    Given string event = {articleId: #(articleId), stock: #(stock)}
    When stockChannel.produce(event)
    Then wait(2000)
