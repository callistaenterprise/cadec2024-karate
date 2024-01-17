package util.data.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;


/**
 * Utility class for accessing a SQL database.
 */
public class DatabaseAccessImpl implements DatabaseAccess {

    protected final DataSource dataSource;

    private final QueryRunner runner;

    private final MapHandler mapHandler = new MapHandler();

    private final MapListHandler mapListHandler = new MapListHandler();

    /**
     * Construct a database accessor for a specific dataSource.
     *
     * @param dataSource dataSource
     */
    public DatabaseAccessImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.runner = new QueryRunner(dataSource);
    }

    @Override
    public Map<String, Object> selectFirst(String sqlQuery) throws SQLException {
        return runner.query(sqlQuery, mapHandler);
    }

    @Override
    public List<Map<String, Object>> select(String sqlQuery) throws SQLException {
        return runner.query(sqlQuery, mapListHandler);
    }

    @Override
    public int update(String sqlUpdate) throws SQLException {
        return runner.update(sqlUpdate);
    }

    @Override
    public void insertInto(String table, Map<String, Object> object) throws SQLException {
        String columnsAndValues = asColumnsAndValues(object);
        runner.update("INSERT INTO " + table + " " + columnsAndValues);
    }

    @Override
    public void insertInto(String table, List<Map<String, Object>> objects) throws SQLException {
        for (Map<String, Object> object : objects) {
            insertInto(table, object);
        }
    }

    @Override
    public void close() {
    }

    /*
     * Convert an object represented as a map into a SQL string on the format
     * "(column1, column2, ...) VALUES (value1, value2, ...) to use in
     * an "INSERT INTO table " SQL statement.
     */
    static String asColumnsAndValues(Map<String, Object> row) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        boolean firstColumn = true;
        for (Map.Entry<String, Object> column : row.entrySet()) {
            columns.append(firstColumn ? "(" : ", ");
            columns.append(column.getKey());
            values.append(firstColumn ? "(" : ", ");
            if (column.getValue() instanceof String) {
                values.append("'");
                values.append(column.getValue());
                values.append("'");
            } else {
                values.append(column.getValue());
            }
            firstColumn = false;
        }
        columns.append(")");
        values.append(")");
        return columns + " VALUES " + values;
    }

}
