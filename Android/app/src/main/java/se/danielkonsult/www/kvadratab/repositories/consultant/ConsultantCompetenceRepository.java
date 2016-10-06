package se.danielkonsult.www.kvadratab.repositories.consultant;

/**
 * Created by Daniel on 2016-10-06.
 */
public interface ConsultantCompetenceRepository {

    /**
     * Gets the competences that belong to a specific consultant.
     */
    String[] getById(int consultantId);

    /**
     * Updates the competences of a consultant.
     */
    void update(int consultantId, String[] competences);
}
