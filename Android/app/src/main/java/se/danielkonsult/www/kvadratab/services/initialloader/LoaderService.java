package se.danielkonsult.www.kvadratab.services.initialloader;

/**
 * Created by Daniel on 2016-09-30.
 */
public interface LoaderService {
    boolean isInitialLoadNeeded();

    void run(LoaderServiceListener listener);
}
