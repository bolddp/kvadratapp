package se.danielkonsult.www.kvadratab.services.data;

import android.graphics.Bitmap;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Listener for data service notifications, for instance when it has been determined
 * that the database should be loaded for the first time or refreshed, and when new
 * data is found on the web page.
 */
public interface DataServiceListener {

    /**
     * Indicates that the list of filtered consultants have been updated, e.g. due to
     * loading or because of a new filter.
     */
    void onFilteredConsultantsUpdated();
}
