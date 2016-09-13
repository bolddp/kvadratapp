package se.danielkonsult.www.kvadratab.repositories.consultant;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;

/**
 * Created by Daniel on 2016-09-13.
 */
public interface ConsultantDataRepository {
    void getAll(DbDataListener<ConsultantData[]> listener);

    void insert(ConsultantData consultant, DbOperationListener listener);
}
