package se.danielkonsult.www.kvadratab.services.prefs;

/**
 * Created by Daniel on 2016-09-24.
 */
public interface PrefsService {

    /**
     * Returns how many hours it was since the last refresh of data
     * was performed.
     */
    int getHoursSinceLastRefresh();

    /**
     * Indicates that a refresh of the app data was just performed.
     */
    void setRefreshPerformed();
}
