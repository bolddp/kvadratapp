package se.danielkonsult.www.kvadratab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.data.DataService;
import se.danielkonsult.www.kvadratab.services.data.DataServiceListeners;
import se.danielkonsult.www.kvadratab.services.data.DefaultDataService;

/**
 * Application-wide singleton with a responsibility to provide services
 * and data to the rest of the application.
 */
public class AppCtrl {

    private static Context _applicationContext;
    private static KvadratDb _db;
    private static DefaultDataService _dataService;

    /**
     * Returns the application context that has been set.
     */
    public static Context getApplicationContext() {
        return _applicationContext;
    }

    public static void setApplicationContext(Context context){
        _applicationContext = context;
    }

    public static KvadratDb getDb() {
        if (_db == null)
            _db = new KvadratDb();
        return _db;
    }

    public static DataService getDataService() {
        if (_dataService == null)
            _dataService = new DefaultDataService();
        return _dataService;
    }
}
