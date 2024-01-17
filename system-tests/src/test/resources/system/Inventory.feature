Feature: Inventory system tests

  Background:
    * configure ssl = read('classpath:/system/sslConfig.json')
    * def stockChannel = eventBus.producer('stock')
    * def levelChannel = karate.get('eventBus') ? eventBus.consumer('level') : null
    * call read('action/TestDataAction.feature@InitialState')

  Scenario: stock should update stock level
    Given def existinglevel = 1000
    And string inventoryEvent = {articleId: 'a101', stock: 100}
    And def expectedNewlevel = existinglevel + 100

    When stockChannel.produce(inventoryEvent)
    Then json levelEvent = levelChannel.consume()
    And match levelEvent.stock == expectedNewlevel

    Given url inventory_url
    And path '/inventory/a101'
    When method get
    Then match response.stock == expectedNewlevel
