package se.danielkonsult.www.kvadratab.helpers.db;

import se.danielkonsult.www.kvadratab.entities.OfficeData;

/**
 * Listener for an array of offices from the database.
 */
public interface OfficeDataArrayListener {
    void onResult(OfficeData[] offices);
    void onError(String errorMessage);
}
