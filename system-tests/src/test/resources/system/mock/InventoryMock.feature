Feature: Inventory Mock

  Scenario: pathMatches('/inventory/{articleId}')
    * def articleId = pathParams.articleId
    * def response = read('response/inventory.json')
