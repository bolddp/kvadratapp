package se.danielkonsult.www.kvadratab.repositories.office;

import se.danielkonsult.www.kvadratab.entities.OfficeData;

/**
 * Handles db reading and writing of office data.
 */
public interface OfficeDataRepository {
    OfficeData getById(int id);
    OfficeData[] getAll();

    void insert(OfficeData officeData);
}
