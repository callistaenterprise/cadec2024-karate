@Ignore
Feature: Product UI reusable scenarios

  @Login
  Scenario: Successful login
    Given driver product_ui_url + '/login'
    And input('#username', username)
    And input('#password', password)
    When click('#login')
    Then waitForUrl(product_ui_url)
    And match driver.url == product_ui_url + '/'

  @Create
  Scenario: Create Product
    * waitForUrl(product_ui_url + '/')
    Given match driver.url == product_ui_url + '/'
    And waitFor('.mdc-linear-progress--closed')
    When click("//button[text()='Add New']")
    Then waitForText('//h2', 'Create New Product')
    Given input('#addArticleId', articleId)
    Given input('#addName', productName)
    When click('#addProduct')
    And delay(1000)
