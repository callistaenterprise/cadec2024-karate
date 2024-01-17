package util.data.dbrider;

import java.util.Map;

import util.data.jdbc.DatabaseAccess;

public interface DBRiderDatabaseAccess extends DatabaseAccess {
    /**
     * Insert a given dataset.
     *
     * @param dataset the dataset, represented as a Map suitable for conversion from JSon
     */
    void insert(Map<String, Object> dataset);

    /**
     * Clean insert a given dataset (i.e. truncating affected tables before insert).
     *
     * @param dataset the dataset, represented as a Map suitable for conversion from JSon
     */
    void cleanInsert(Map<String, Object> dataset);

    /**
     * Update a given dataset.
     *
     * @param dataset the dataset, represented as a Map suitable for conversion from JSon
     */
    void update(Map<String, Object> dataset);

    /**
     * Refresh a given dataset (i.e. insert missing rows, update existing rows).
     *
     * @param dataset the dataset, represented as a Map suitable for conversion from JSon
     */
    void refresh(Map<String, Object> dataset);

    /**
     * Verify that the actual database content is equal to the given dataset.
     *
     * @param dataset the expected dataset
     */
    void verifyEquals(Map<String, Object> dataset);

    /**
     * Verify that the actual database content is equal to the given dataset.
     *
     * @param dataset the expected dataset
     */
    void verifyContains(Map<String, Object> dataset);

    /**
     * Verify given database expectations.
     *
     * @param verifyConfig the verify configuration, represented as a Map with keys for
     *                     <ul>
     *                       <li>dataset - the dataset (required)
     *                       <li>operation - verify operation EQUALS or CONTAINS (default EQUALS)
     *                       <li>ignore - comma-separated string of columns to ignore in the verification
     *                       <li>orderBy - comma-separated string of columns to order by before verification
     *                     </ul>
     */
    void verify(Map<String, Object> verifyConfig);
}
