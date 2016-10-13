package se.danielkonsult.www.kvadratab.services.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.services.data.ConsultantFilter;

/**
 * Created by Daniel on 2016-09-24.
 */
public class DefaultPrefsService implements PrefsService {

    // Private variables

    private static final String PREF_INITIAL_LOAD_PERFORMED = "pref_initial_load_performed";
    private static final String PREF_IMAGE_COMPARISON_TIMESTAMP = "pref_image_comparison_timestamp";
    private static final String PREF_TEST_MODE = "pref_test_mode";
    private static final String PREF_CONSULTANT_FILTER = "pref_consultant_filter";

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

    @Override
    public boolean getTestMode() {
        return _prefs.getBoolean(PREF_TEST_MODE, false);
    }

    @Override
    public void setTestMode(boolean value) {
        _prefs.edit().putBoolean(PREF_TEST_MODE, value).commit();
    }

    @Override
    public ConsultantFilter getConsultantFilter() {
        String jsonString = _prefs.getString(PREF_CONSULTANT_FILTER, null);
        if (Utils.isStringNullOrEmpty(jsonString))
            return null;

        try {
            return new Gson().fromJson(jsonString, ConsultantFilter.class);
        } catch (Exception ex) {
            _prefs.edit().putString(PREF_CONSULTANT_FILTER, "").commit();
            return null;
        }
    }

    @Override
    public void setConsultantFilter(ConsultantFilter filter) {
        String jsonString = "";
        if (filter != null) {
            jsonString = new Gson().toJson(filter);
        }

        _prefs.edit().putString(PREF_CONSULTANT_FILTER, jsonString).commit();
    }
}
