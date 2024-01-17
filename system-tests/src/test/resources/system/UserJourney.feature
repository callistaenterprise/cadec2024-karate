Feature: Product User Journey tests

  Background:
    * configure ssl = read('classpath:/system/sslConfig.json')
    * def basicAuth = call read('classpath:/system/basicAuth.js') { username: 'client', password: 'secret' }
    * configure headers = { Authorization: #(basicAuth) }
    * def stockChannel = eventBus.producer('stock')
    * def levelChannel = eventBus.consumer('level')
    # 0: Restore initial known system state
    * call read('action/TestDataAction.feature@InitialState')

  Scenario: Create Product and add stock
    Given def articleId = "new"
    And def productName = 'New Product'

    # 1: Create a new Product using the Product UI
    Given call read('action/ProductUIAction.feature@Login') {username: 'admin', password: 'secret'}
    Then call read('action/ProductUIAction.feature@Create') {name: #(productName), articleId: #(articleId)}
    And driver.quit()

    # 2: Retrieve the created Product using the Product API
    Given def productApi = call read('action/ProductAction.feature@GetProduct') {articleId: #(articleId)}
    And match productApi.response contains {name: #(productName), articleId: #(articleId), inventory: 0}
    And wait(2000)

    # 3: Verify stock for the created Product using the Inventory API
    Given def inventoryApi = call read('action/InventoryAction.feature@GetInventory') {articleId: #(articleId)}
    Then match inventoryApi.response.stock == 0

    # 4: Replenish stock for the product using the stock Event Channel
    Given def newStock = 10000
    Then call read('action/InventoryAction.feature@Replenish') {articleId: #(articleId), stock: #(newStock)}

    # 5: Verify the updated inventory using the Product API
    Given def productApi = call read('action/ProductAction.feature@GetProduct') {articleId: #(articleId)}
    Then match productApi.response.inventory == '#(newStock)'
