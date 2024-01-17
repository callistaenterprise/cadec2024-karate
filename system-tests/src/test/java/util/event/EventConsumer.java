package util.event;

public interface EventConsumer {
    long DEFAULT_TIMEOUT_MILLISECONDS = 5000;

    /**
     * Wait for and consume next message from queue. If no message arrives within
     * the default timeout interval, return null.
     *
     * @return the consumed message as a string, or null if no message available.
     */
    Object consume();

    /**
     * Wait for and consume next message from queue. If no message arrives within
     * the timeout interval, return null.
     *
     * @param waitTimeout timeout interval i milliseconds
     * @return the consumed message as a string, or null if no message available.
     */
    Object consume(long waitTimeout);

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
     * Close the queue consumer.
     */
    void close();
}
