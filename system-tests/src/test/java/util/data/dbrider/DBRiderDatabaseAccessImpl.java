package util.data.dbrider;

import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.core.configuration.DBUnitConfig;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.configuration.ExpectedDataSetConfig;
import com.github.database.rider.core.dsl.RiderDSL;

import util.data.DatabaseAccess;
import util.data.jdbc.DatabaseAccessImpl;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import com.github.database.rider.core.api.dataset.MapDataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Utility class for accessing a SQL database using DBRider.
 */
public class DBRiderDatabaseAccessImpl extends DatabaseAccessImpl
  implements DatabaseAccess, DBRiderDatabaseAccess {

    /**
     * Construct a database accessor for a specific database.
     *
     * @param dataSource dataSource
     */
    public DBRiderDatabaseAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insert(Map<String, Object> dataset)  {
        execute(SeedStrategy.INSERT, new MapDataSet(dataset));
    }

    @Override
    public void restoreState(Map<String, Object> dataset)  {
        cleanInsert(dataset);
    }

    @Override
    public void cleanInsert(Map<String, Object> dataset)  {
        execute(SeedStrategy.CLEAN_INSERT, new MapDataSet(dataset));
    }

    @Override
    public void update(Map<String, Object> dataset)  {
        execute(SeedStrategy.UPDATE, new MapDataSet(dataset));
    }

    @Override
    public void refresh(Map<String, Object> dataset)  {
        execute(SeedStrategy.REFRESH, new MapDataSet(dataset));
    }

    @Override
    public void verifyEquals(Map<String, Object> dataset) {
        String[] ignore = new String[0];
        String[] orderBy =new String[0];
        verify(new MapDataSet(dataset), CompareOperation.EQUALS, ignore, orderBy);
    }

    @Override
    public void verifyContains(Map<String, Object> dataset) {
        String[] ignore = new String[0];
        String[] orderBy =new String[0];
        verify(new MapDataSet(dataset), CompareOperation.CONTAINS, ignore, orderBy);
    }

    @Override
    public void verify(Map<String, Object> verifyConfig) {
        @SuppressWarnings("unchecked")
        Map<String, Object> dataset = (Map<String, Object>) verifyConfig.get("dataset");
        String operation = (String)verifyConfig.get("operation");
        CompareOperation compareOperation = operation != null ? CompareOperation.valueOf(operation): CompareOperation.EQUALS;
        String ignore = (String)verifyConfig.get("ignore");
        String[] ignoreCols = ignore != null ? ignore.split("\\s*,\\s*") : new String[0];
        String orderBy = (String)verifyConfig.get("orderBy");
        String[] orderByCols = orderBy != null ? orderBy.split("\\s*,\\s*") : new String[0];
        verify(new MapDataSet(dataset), compareOperation, ignoreCols, orderByCols);
    }

    private void execute(SeedStrategy strategy, IDataSet dataset)  {
        try (Connection jdbcConnection = dataSource.getConnection()) {
            RiderDSL
                .withConnection(jdbcConnection)
                .withDataSetConfig(new DataSetConfig()
                    .datasetProvider(() -> dataset)
                    .strategy(strategy)
                )
                .withDBUnitConfig(
                    DBUnitConfig.fromGlobalConfig().cacheConnection(false)
                )
                .createDataSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void verify(IDataSet expected, CompareOperation compareOperation, String[] ignoreCols, String[] orderBy)  {
        try (Connection jdbcConnection = dataSource.getConnection()) {
            RiderDSL
                .withConnection(jdbcConnection)
                .withDataSetConfig(new DataSetConfig()
                    .datasetProvider(() -> expected)
                )
                .withDBUnitConfig(
                    DBUnitConfig.fromGlobalConfig().cacheConnection(false)
                )
                .expectDataSet(new ExpectedDataSetConfig()
                    .ignoreCols(ignoreCols)
                    .orderBy(orderBy)
                    .compareOperation(compareOperation)
                );
        } catch (DatabaseUnitException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
