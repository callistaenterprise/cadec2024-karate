Feature: Product system tests

  Background:
    * configure ssl = read('classpath:/system/sslConfig.json')
    * def basicAuth = call read('classpath:/system/basicAuth.js') { username: 'client', password: 'secret' }
    * configure headers = { Authorization: #(basicAuth) }
    * call read('action/TestDataAction.feature@InitialState')

  Scenario: get non-existing product
    Given url product_url
    And path '/products/non-existing'
    When method get
    Then status 404

  Scenario: get existing product
    Given url product_url
    And path '/products/a101'
    When method get
    Then status 200
    And match response.name == 'Product 101'

  Scenario Outline: get existing product <articleId>
    Given url product_url
    And path '/products/<articleId>'
    When method get
    Then status 200
    And match response contains {name: '<name>', inventory: <inventory>}

    Examples:
    | articleId | name        | inventory |
    | a101      | Product 101 | 1000      |
    | a102      | Product 102 | 2000      |
    | a103      | Product 103 | 3000      |

  Scenario: create a product
    Given url product_url
    And path '/products/'
    And def articleId = 'new'
    And def name = 'a new product'
    And request {name: #(name), articleId: #(articleId)}
    When method post
    Then status 201
    And match response contains {productId: #number, name: #(name), inventory: 0}
