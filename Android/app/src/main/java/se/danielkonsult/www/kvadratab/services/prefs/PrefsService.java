package se.danielkonsult.www.kvadratab.services.prefs;

import se.danielkonsult.www.kvadratab.services.data.ConsultantFilter;

/**
 * Created by Daniel on 2016-09-24.
 */
public interface PrefsService {

    boolean getHasInitialLoadingBeenPerformed();

    void setHasInitialLoadingBeenPerformed(boolean value);

    long getImageComparisonTimestamp();

    void setImageComparisonTimestamp(long timestamp);

    boolean getTestMode();

    void setTestMode(boolean value);

    ConsultantFilter getConsultantFilter();

    void setConsultantFilter(ConsultantFilter filter);
}
