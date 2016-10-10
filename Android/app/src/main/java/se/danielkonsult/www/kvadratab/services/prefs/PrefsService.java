package se.danielkonsult.www.kvadratab.services.prefs;

/**
 * Created by Daniel on 2016-09-24.
 */
public interface PrefsService {

    boolean getHasInitialLoadingBeenPerformed();

    void setHasInitialLoadingBeenPerformed(boolean value);

    long getImageComparisonTimestamp();

    void setImageComparisonTimestamp(long lastImageComparisonTimestamp);
}
