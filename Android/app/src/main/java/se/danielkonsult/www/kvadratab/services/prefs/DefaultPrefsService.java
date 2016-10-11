package se.danielkonsult.www.kvadratab.services.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import se.danielkonsult.www.kvadratab.AppCtrl;

/**
 * Created by Daniel on 2016-09-24.
 */
public class DefaultPrefsService implements PrefsService {

    // Private variables

    private static final String PREF_INITIAL_LOAD_PERFORMED = "pref_initial_load_performed";
    private static final String PREF_IMAGE_COMPARISON_TIMESTAMP = "pref_image_comparison_timestamp";

    private SharedPreferences _prefs = PreferenceManager.getDefaultSharedPreferences(AppCtrl.getApplicationContext());

    // Public methods

    @Override
    public boolean getHasInitialLoadingBeenPerformed() {
        return _prefs.getBoolean(PREF_INITIAL_LOAD_PERFORMED, false);
    }

    public void setHasInitialLoadingBeenPerformed(boolean value){
        _prefs.edit().putBoolean(PREF_INITIAL_LOAD_PERFORMED, value).commit();
    }

    @Override
    public long getImageComparisonTimestamp() {
        return _prefs.getLong(PREF_IMAGE_COMPARISON_TIMESTAMP, 0);
    }

    @Override
    public void setImageComparisonTimestamp(long timestamp) {
        _prefs.edit().putLong(PREF_IMAGE_COMPARISON_TIMESTAMP, timestamp).commit();
    }
}
