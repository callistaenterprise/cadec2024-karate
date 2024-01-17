/**
 * Wrapper API for creating an ActiveMQ ConnectionManager.
 * @param {string} brokerUrl ActiveMQ broker url
 * @returns an instance of ActiveMQConnectionFactory
 * @example
 * def activemq = karate.call('classpath:/util/jms/activeMq.js', 'tcp://activemq:61616')
 */
function activemq(brokerUrl) {
  var ActiveMQConnectionFactory = Java.type('org.apache.activemq.ActiveMQConnectionFactory');
  return new ActiveMQConnectionFactory(brokerUrl);
}