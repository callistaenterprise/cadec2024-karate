package util.event.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.BytesMessage;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import util.event.EventProducer;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

/**
 * Utility class for producing JMS messages.
 */
public class QueueProducer implements EventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(QueueProducer.class);

    protected final String queueName;
    protected final boolean purge;
    protected final Connection connection;
    protected final Session session;
    protected final Queue destination;
    protected final MessageProducer producer;
    protected MessageConsumer consumer;
    protected QueueBrowser browser;

    /**
     * Construct a queue consumer for a specific queue on a specific broker.
     *
     * @param connectionFactory JMS connectionFactory
     * @param queueName Queue name
     */
    public QueueProducer(ConnectionFactory connectionFactory, String queueName) {
        this(connectionFactory, queueName, true);
    }

    /**
     * Construct a queue consumer for a specific queue on a specific broker, specifying
     * whether queue should be purged before starting and stopping the producer.
     *
     * @param connectionFactory JMS connectionFactory
     * @param queueName Queue name
     * @param purge purge the queue before starting and stopping the producer
     */
    public QueueProducer(ConnectionFactory connectionFactory, String queueName, boolean purge) {
        this.queueName = queueName;
        this.purge = purge;
        this.connection = ConnectionManager.getConnection(connectionFactory);
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            if (purge) {
                purge();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void produce(Object message) {
        produce(message, null, null, null, null);
    }

    @Override
    public void produce(Object message, Map<String, String> properties) {
        produce(message, properties, null, null, null);
    }

    @Override
    public void produce(Object message, Map<String, String> properties,
                     String correlationId, String replyTo, Integer delayMillis) {
        try {
            if (delayMillis != null && delayMillis > 0) {
                LOG.info("*** scheduled delay: {}", delayMillis);
                Thread.sleep(delayMillis);
            }
            Message jmsMessage;
            if (message instanceof byte[]) {
                jmsMessage = session.createBytesMessage();
                ((BytesMessage) jmsMessage).writeBytes((byte[]) message);
            } else {
                jmsMessage = session.createTextMessage(message.toString());
            }
            if (properties != null) {
                for (Map.Entry<String, String> property : properties.entrySet()) {
                    jmsMessage.setStringProperty(property.getKey(), property.getValue());
                }
            }
            if (correlationId != null) {
                jmsMessage.setJMSCorrelationID(correlationId);
            }
            if (replyTo != null) {
                Destination replyToDestination = session.createQueue(replyTo);
                jmsMessage.setJMSReplyTo(replyToDestination);
            }
            producer.send(jmsMessage);
            if (message instanceof byte[]) {
                LOG.info("*** sent byte[] message: {}", new String((byte[]) message, StandardCharsets.UTF_8));
            } else {
                LOG.info("*** sent message: {}", message);
            }
        } catch (Exception e) {
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
        } finally {
            closeBrowser();
        }
    }

    @Override
    public final void purge() {
        try {
            if (consumer == null) {
                consumer = session.createConsumer(destination);
            }
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
        } finally {
            closeConsumer();
        }
    }

    @Override
    public void close() {
        if (purge) {
            purge();
        }
        closeProducer();
        closeConsumer();
        closeBrowser();
        closeSession();
    }

    private void closeProducer() {
        if (producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                LOG.error("Exception when closing producer: ", e);
            }
        }
    }

    private void closeConsumer() {
        if (consumer != null) {
            try {
                consumer.close();
                consumer = null;
            } catch (JMSException e) {
                LOG.error("Exception when closing producer: ", e);
            }
        }
    }

    private void closeBrowser() {
        if (browser != null) {
            try {
                browser.close();
                browser = null;
            } catch (JMSException e) {
                LOG.error("Exception when closing browser: ", e);
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
