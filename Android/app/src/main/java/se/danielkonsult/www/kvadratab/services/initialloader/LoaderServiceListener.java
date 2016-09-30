package se.danielkonsult.www.kvadratab.services.initialloader;

import android.graphics.Bitmap;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Listener for events that are triggered by the LoaderService.
 */
public interface LoaderServiceListener {

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
     * Indicates that a new consultant was found during initial loading.
     */
    void onConsultantAdded(ConsultantData consultant, Bitmap bitmap);

    /**
     * Indicates that the initial loading has finished. This method is also called when
     * it's been determined that no initial loading is necessary.
     */
    void onInitialLoadingCompleted();

    /**
     * Called when an error has occured during initial loading
     * @param tag The module that the error occured in
     * @param errorMessage The message for the error that occured.
     */
    void onError(String tag, String errorMessage);
}
