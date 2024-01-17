package util.event;

import java.util.Map;

public interface EventProducer {
    /**
     * Send a message.
     *
     * @param message The message, either as a String or as a Byte Array
     */
    void produce(Object message);

    /**
     * Send a message with custom properties.
     *
     * @param message    The message, either as a String or as a Byte Array
     * @param properties message properties
     */
    void produce(Object message, Map<String, String> properties);

    /**
     * Send a message with custom properties, correlationId, replyTo and delay.
     *
     * @param message       The message, either as a String or as a Byte Array
     * @param properties    message properties
     * @param correlationId Correlation id message header
     * @param replyTo       replyTo message header
     * @param delayMillis   Delay in milliseconds before sending the message
     */
    void produce(Object message, Map<String, String> properties,
              String correlationId, String replyTo, Integer delayMillis);

    /**
     * Count the messages on the queue.
     *
     * @return number of messages currently on the queue
     */
    int size();

    /**
     * Discard any messages still on the queue.
     */
    void purge();

    /**
     * Close the producer.
     */
    void close();
}
