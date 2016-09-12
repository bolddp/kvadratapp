package se.danielkonsult.www.kvadratab.repositories.office;

import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;

/**
 * Handles db reading and writing of office data.
 */
public interface OfficeDataRepository {
    void getById(int id, DbDataListener<OfficeData> listener);
    void getAll(DbDataListener<OfficeData[]> listener);

    void insert(OfficeData officeData, DbOperationListener listener);
}
