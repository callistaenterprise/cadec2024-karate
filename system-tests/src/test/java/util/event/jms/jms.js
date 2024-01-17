/**
 * API for interacting with JMS Queues within the Karate test framework.
 * @param {Object} connectionFactory Java ConnectionFactory instance
 * @returns  a map of API methods.
 * @example
 * def activemq = karate.call('classpath:/util/jms/activeMq.js', 'tcp://activemq:61616')
 * def jms = karate.call('classpath:/util/jms/jms.js', activemq)
 */
function jms(connectionFactory) {
  return {
    /**
     * Create a JMS event producer for a specific destination queue.
     * @param {string} destination Queue name
     * @returns a QueueProducer object
     * @example
     * def producer = jms.producer('SomeQueue')
     * producer.produce('A message')
     * def delayMillis = 1000
     * producer.produce(
     *   'Another message', 
     *   {'customProperty': 'customValue'},
     *   'myCorrelationId',
     *   'ReplyToQueue',
     *   delayMillis
     * )
     * def queueDepth = producer.size()
     * producer.purge()
     */
    "producer": function(destination) {
      var QueueProducer = Java.type('util.event.jms.QueueProducer');
      return new QueueProducer(connectionFactory, destination);
    },
    /**
     * Create a JMS event consumer for a specific destination queue.
     * The consumer is passive, i.e. it only consumes messages from the queue when
     * explicitly asked to.
     * @param {string} destination Queue name
     * @returns a QueueConsumer object
     * @example
     * def consumer = jms.consumer('SomeQueue')
     * def aMessage = consumer.consume()
     * def timeout = 5000
     * def anmotherMessage = consumer.consume(timeout)
     * def queueDepth = consumer.size()
     * producer.purge()
     */
    "consumer": function(destination) {
      var QueueConsumer = Java.type('util.event.jms.QueueConsumer');
      return new QueueConsumer(connectionFactory, destination);
    }
  }
}