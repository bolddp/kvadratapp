package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Application-wide singleton with a responsibility to provide services
 * and data to the rest of the application.
 */
public class AppCtrl {

    private static Context _applicationContext;
    private static boolean _shouldUseTestDb;

    /**
     * Returns the application context that has been set.
     */
    public static Context getApplicationContext() {
        return _applicationContext;
    }

    public static void setApplicationContext(Context context){
        _applicationContext = context;
    }
}
