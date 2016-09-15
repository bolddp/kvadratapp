package se.danielkonsult.www.kvadratab.services.data;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Created by Daniel on 2016-09-14.
 */
public interface DataService {

    void registerListener(DataServiceListener listener);

    void unregisterListener(DataServiceListener listener);

    /**
     * Starts the data service, triggering it into starting to notify
     * listeners of the current state of the database.
     */
    void start();

    /**
     * Sets the consultants that are cached by the data service.
     */
    void setConsultants(ConsultantData[] consultants);

    /**
     * Gets a list of all consultants that is cached in the data service.
     */
    ConsultantData[] getConsultants();
}
