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
     * Called when the Kvadrat db has been determined to be empty and initial
     * loading of data scraping has started.
     */
    void onInitialLoadStarted();

    /**
     * Called when it has been determined how many consultants are available,
     * and each time a new consultant has been fully loaded (e.g. the consultant
     * image since the rest of the data is pretty general).
     */
    void onInitialLoadProgress(int progressCount, int totalCount);

    /**
     * Indicates that a new consultant was found and fully loaded, either during initial load or
     * during a refresh of the database.
     */
    void onConsultantAdded(ConsultantData consultant, Bitmap bitmap);

    /**
     * Indicates that the data service has loaded, either after performing an initial load
     * or determining that all relevant data is available already.
     */
    void onLoaded();

    /**
     *
     * @param tag The module that the error occured in
     * @param errorMessage The message for the error that occured.
     */
    void onError(String tag, String errorMessage);
}
