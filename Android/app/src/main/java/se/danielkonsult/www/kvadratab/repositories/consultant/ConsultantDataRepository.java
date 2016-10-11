package se.danielkonsult.www.kvadratab.repositories.consultant;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;

/**
 * Created by Daniel on 2016-09-13.
 */
public interface ConsultantDataRepository {

    // Methods

    ConsultantData getById(int id, boolean joinOffice);

    ConsultantData[] getAll(boolean joinOffices);

    /**
     * Gets the total number of consultants in the database.
     */
    int getCount();

    /**
     * Inserts a new consultant.
     */
    void insert(ConsultantData consultant);

    /**
     * Updates the office id of a specific consultant.
     */
    void updateOffice(int consultantId, int officeId);

    /**
     * Updates the name of a consultant.
     */
    void updateName(int consultantId, String firstName, String lastName);

    /**
     * Updated the details of the consultant.
     */
    void updateDetails(int consultantId, ConsultantDetails details);

    /**
     * Deletes a consultant by its id.
     */
    void delete(int id);
}
