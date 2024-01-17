@Ignore
Feature: Product reusable scenarios

  @GetProduct
  Scenario: get existing product (reusable scenario)
    Given url product_url
    And path '/products/' + articleId
    When method get
    Then status 200
