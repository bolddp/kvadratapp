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

    private static final String PREF_LASTREFRESH_EPOCH = "pref_lastrefresh_epoch";

    private SharedPreferences _prefs = PreferenceManager.getDefaultSharedPreferences(AppCtrl.getApplicationContext());

    // Public methods

    @Override
    public int getHoursSinceLastRefresh() {
        long lastRefreshEpoch = _prefs.getLong(PREF_LASTREFRESH_EPOCH, 0);
        long currentTimeEpoch = System.currentTimeMillis()/1000;

        long secondsSinceLastRefresh = (currentTimeEpoch - lastRefreshEpoch);

        return (int) (secondsSinceLastRefresh / 3600);
    }

    @Override
    public void setRefreshPerformed() {
        long currentTimeEpoch = System.currentTimeMillis()/1000;
        _prefs.edit().putLong(PREF_LASTREFRESH_EPOCH, currentTimeEpoch).commit();
    }
}
