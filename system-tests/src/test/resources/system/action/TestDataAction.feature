@Ignore
Feature: Test Data Management reusable scenarios

  @InitialState
  Scenario: Restore Test Data to known state
    * def initialProducts = read('classpath:/data/initialProducts.csv')
    * productDb.dbUnit.restoreState({product: initialProducts});
    * def initialInventory = read('classpath:/data/initialInventory.csv')
    * inventoryDb.dbUnit.restoreState({inventory: initialInventory});
