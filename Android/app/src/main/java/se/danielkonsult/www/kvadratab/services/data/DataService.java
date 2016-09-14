package se.danielkonsult.www.kvadratab.services.data;

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
}
