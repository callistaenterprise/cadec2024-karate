package util.event.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing JMS connections.
 */
public final class ConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

    private static final Map<ConnectionFactory, Connection> CONNECTION_CACHE = new HashMap<>();

    private ConnectionManager() { }

    /**
     * Get JMS connection from a given ConnectionFactory.
     * @param connectionFactory JMS connectionFactory
     * @return the connection for the given url
     */
    public static Connection getConnection(ConnectionFactory connectionFactory) {
        Connection connection = CONNECTION_CACHE.get(connectionFactory);
        if (connection == null) {
            try {
                LOG.debug("Waiting for JMS connection ...");
                connection = connectionFactory.createConnection();
                connection.start();
                CONNECTION_CACHE.put(connectionFactory, connection);
                LOG.debug("JMS connection established");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    /**
     * Close ActiveMQ connection for a given broker url.
     * @param connectionFactory JMS connectionFactory
     */
    public static void closeConnection(ConnectionFactory connectionFactory) {
        Connection connection = CONNECTION_CACHE.get(connectionFactory);
        if (connection != null) {
            try {
                connection.stop();
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                CONNECTION_CACHE.remove(connectionFactory);
            }
        }
    }

}
