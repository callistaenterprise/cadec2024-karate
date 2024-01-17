package util.data.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseAccess {
    /**
     * Perform a sql query for a single row, and return the (first) row as a map, suitable
     * for conversion into JSon.
     *
     * @param sqlQuery the sql query
     * @return the first resulting row as a map, or null if empty result
     * @throws SQLException on database errors
     */
    Map<String, Object> selectFirst(String sqlQuery) throws SQLException;

    /**
     * Perform a sql query, and return the result as a list of maps, suitable
     * for conversion into JSon.
     *
     * @param sqlQuery the sql query
     * @return the result set as a list of maps
     * @throws SQLException on database errors
     */
    List<Map<String, Object>> select(String sqlQuery) throws SQLException;

    /**
     * Perform a sql update, and return the number of affected rows.
     *
     * @param sqlUpdate the sql query
     * @return the number of affected rows
     * @throws SQLException on database errors
     */
    int update(String sqlUpdate) throws SQLException;

    /**
     * Insert an object into a specific table.
     *
     * @param table  the table
     * @param object the object represented as a map (which is suitable for
     *               conversion from JSon)
     * @throws SQLException on database errors
     */
    void insertInto(String table, Map<String, Object> object) throws SQLException;

    /**
     * Insert a list of objects into a specific table.
     *
     * @param table   the table
     * @param objects the objects represented as a list of maps (which is suitable for
     *                conversion from JSon)
     * @throws SQLException on database errors
     */
    void insertInto(String table, List<Map<String, Object>> objects) throws SQLException;

    /**
     * Close the database accessor.
     */
    void close();
}
