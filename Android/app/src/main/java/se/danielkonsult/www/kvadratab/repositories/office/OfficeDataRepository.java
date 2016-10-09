package se.danielkonsult.www.kvadratab.repositories.office;

import se.danielkonsult.www.kvadratab.entities.OfficeData;

/**
 * Handles db reading and writing of office data.
 */
public interface OfficeDataRepository {
    OfficeData getById(int id);
    OfficeData[] getAll();

    void insert(OfficeData officeData);

    /**
     * Updates the data of an office.
     * @param officeId The if of the office that should be updated
     * @param name The new name of the office
     */
    void update(int officeId, String name);
}
