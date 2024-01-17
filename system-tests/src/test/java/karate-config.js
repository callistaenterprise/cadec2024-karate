function fn() {
  var config = {}

  config.wait = function(pause) { java.lang.Thread.sleep(pause) };

  var env = karate.env; // get system property 'karate.env'
  if (!env) {
    env = 'dev';
  }

  if (env == 'dev') {
    karate.configure('afterScenario', read('classpath:/system/afterScenario.js'));

    config.product_ui_url = 'https://localhost';
    config.product_url = 'https://host.docker.internal:8443';
    config.inventory_url = 'https://host.docker.internal:9443';

    // Manage events using JMS/ActiveMQ
    var brokerUrl = 'tcp://host.docker.internal:61616';
    var activemq = karate.callSingle('classpath:/util/event/jms/activemq.js', brokerUrl);
    config.eventBus = karate.callSingle('classpath:/util/event/jms/jms.js', activemq);

    // Manage test data using JDBC/DbUnit
    var productDs = karate.callSingle('classpath:/util/data/jdbc/dataSource.feature?product',
      {"jdbcUrl": 'jdbc:postgresql://host.docker.internal:5432/product',
        "username": 'admin', "password": 'secret'});
    var inventoryDs = karate.callSingle('classpath:/util/data/jdbc/dataSource.feature?inventory',
      {"jdbcUrl": 'jdbc:postgresql://host.docker.internal:5432/inventory',
        "username": 'admin', "password": 'secret'});
    config.productDb = karate.callSingle('classpath:/util/data/dbrider/databaseAccess.feature?product', {dataSource: productDs.dataSource});
    config.inventoryDb = karate.callSingle('classpath:/util/data/dbrider/databaseAccess.feature?inventory', {dataSource: inventoryDs.dataSource});

  } else if (env == 'performance') {
    config.product_url = 'https://host.docker.internal:8443';
    config.inventory_url = 'https://host.docker.internal:9443';
  }

  return config;
}