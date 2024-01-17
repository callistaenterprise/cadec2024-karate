package util.event.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.BytesMessage;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import util.event.EventConsumer;

import java.util.Enumeration;

/**
 * Utility class for consuming JMS messages using explicit fetch.
 */
public class QueueConsumer implements EventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(QueueConsumer.class);

    protected final String queueName;
    protected final boolean purge;
    protected final Connection connection;
    protected final Queue destination;
    protected final Session session;
    protected final MessageConsumer consumer;
    protected QueueBrowser browser;
    protected final long timeout;

    /**
     * Construct a queue consumer for a specific queue on a specific broker.
     *
     * @param connectionFactory JMS connectionFactory
     * @param queueName Queue name
     */
    public QueueConsumer(ConnectionFactory connectionFactory, String queueName) {
        this(connectionFactory, queueName, DEFAULT_TIMEOUT_MILLISECONDS, true);
    }

    /**
     * Construct a queue consumer for a specific queue on a specific broker, specifying
     * default timeout time and whether queue should be purged before starting and stopping
     * the consumer.
     *
     * @param connectionFactory JMS connectionFactory
     * @param queueName Queue name
     * @param timeoutInMilliseconds default timeout
     * @param purge purge the queue before starting and stopping the consumer
     */
    public QueueConsumer(ConnectionFactory connectionFactory, String queueName, long timeoutInMilliseconds, boolean purge) {
        this.queueName = queueName;
        this.purge = purge;
        this.connection = ConnectionManager.getConnection(connectionFactory);
        this.timeout = timeoutInMilliseconds;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
            if (purge) {
                purge();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object consume() {
        return consume(timeout);
    }

    @Override
    public Object consume(long waitTimeout) {
        try {
            Message message = consumer.receive(waitTimeout);
            if (message != null) {
                if (message instanceof BytesMessage bm) {
                    byte[] resultBytes = new byte[(int) bm.getBodyLength()];
                    bm.readBytes(resultBytes);
                    return resultBytes;
                } else {
                    TextMessage tm = (TextMessage) message;
                    return tm.getText();
                }
            } else {
                return null;
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try {
            if (browser == null) {
                browser = session.createBrowser(destination);
            }
            int result = 0;
            @SuppressWarnings("unchecked")
            Enumeration<Message> messages = browser.getEnumeration();
            while (messages.hasMoreElements()) {
                messages.nextElement();
                result++;
            }
            return result;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void purge() {
        try {
            while (true) {
                Message message = consumer.receive(50);
                if (message == null) {
                    LOG.debug("*** no more messages to purge: {}", queueName);
                    break;
                }
                LOG.info("*** purged message: {} - {}", queueName, message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (purge) {
            purge();
        }
        closeConsumer();
        closeSession();
    }

    private void closeConsumer() {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (JMSException e) {
                LOG.error("Exception when closing producer: ", e);
            }
        }
    }

    private void closeSession() {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                LOG.error("Exception when closing session: ", e);
            }
        }
    }

}
