package util.data.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Utility class for building DataSources.
 * Uses HikariCP as DataSource implementation.
 */
public class DataSourceBuilder {
    
    /**
     * Create a DataSource based on the supplied config parameters.
     * @param config Map containing entries for jdbcUrl, username and password.
     * @return the datasource.
     */
    public static DataSource dataSource(Map<String, String> config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.get("jdbcUrl"));
        hikariConfig.setUsername(config.get("username"));
        hikariConfig.setPassword(config.get("password"));
        return new HikariDataSource(hikariConfig);
    }

}
