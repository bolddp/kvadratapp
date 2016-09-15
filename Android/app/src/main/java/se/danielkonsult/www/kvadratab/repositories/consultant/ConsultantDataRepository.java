package se.danielkonsult.www.kvadratab.repositories.consultant;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Created by Daniel on 2016-09-13.
 */
public interface ConsultantDataRepository {
    ConsultantData getById(int id, boolean joinOffice);

    ConsultantData[] getAll(boolean joinOffices);

    /**
     * Gets the total number of consultants in the database.
     */
    int getCount();

    void insert(ConsultantData consultant);

    /**
     * Updates the office id of a specific consultant.
     */
    void updateOffice(int consultantId, int officeId);
}
