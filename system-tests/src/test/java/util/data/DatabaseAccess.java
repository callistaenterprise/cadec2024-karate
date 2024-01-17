package util.data;

import java.util.Map;

public interface DatabaseAccess {

    /**
     * Restore state to a given dataset (i.e. truncating affected tables before insert).
     *
     * @param dataset the dataset, represented as a Map suitable for conversion from JSon
     */
    void restoreState(Map<String, Object> dataset);

}
