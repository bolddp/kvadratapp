package se.danielkonsult.www.kvadratab.mocks;

import se.danielkonsult.www.kvadratab.services.data.ConsultantFilter;
import se.danielkonsult.www.kvadratab.services.prefs.PrefsService;

/**
 * Created by Daniel on 2016-10-11.
 */
public class TestPrefsService implements PrefsService {

    private long _imageComparisonTimestamp;
    private boolean _hasInitialLoadingBeenPerformed;

    @Override
    public boolean getHasInitialLoadingBeenPerformed() {
        return _hasInitialLoadingBeenPerformed;
    }

    @Override
    public void setHasInitialLoadingBeenPerformed(boolean value) {
        _hasInitialLoadingBeenPerformed = value;
    }

    @Override
    public long getImageComparisonTimestamp() {
        return _imageComparisonTimestamp;
    }

    @Override
    public void setImageComparisonTimestamp(long timestamp) {
        _imageComparisonTimestamp = timestamp;
    }

    @Override
    public boolean getTestMode() {
        return false;
    }

    @Override
    public void setTestMode(boolean value) {
        // No code
    }

    @Override
    public ConsultantFilter getConsultantFilter() {
        return new ConsultantFilter();
    }

    @Override
    public void setConsultantFilter(ConsultantFilter filter) {
        // No code
    }
}
