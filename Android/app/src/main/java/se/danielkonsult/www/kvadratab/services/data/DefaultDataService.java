package se.danielkonsult.www.kvadratab.services.data;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Created by Daniel on 2016-09-14.
 */
public class DefaultDataService implements DataService {

    // Private variables

    private final DataServiceListeners _listeners = new DataServiceListeners();

    /**
     * Performs the initial load of data by scraping the web page and
     * storing the found data in the database.
     */
    private void performInitialLoad() {
        // Notify listeners
        _listeners.onInitialLoadStarted();
    }

    private void performRefresh() {

    }

    @Override
    public void registerListener(DataServiceListener listener) {
        _listeners.registerListener(listener);
    }

    @Override
    public void unregisterListener(DataServiceListener listener) {
        _listeners.unregisterListener(listener);
    }

    @Override
    public void start() {
        // Start on a separate thread to not lock up GUI
        Runnable startRunnable = new Runnable() {
            @Override
            public void run() {
                // Do we have any consultants in the database yet?
                KvadratDb db = AppCtrl.getDb();
                int consultantCount = db.getConsultantCount();

                if (consultantCount == 0) {
                    performInitialLoad();
                }
                else
                    performRefresh();
            }
        };
        AsyncTask.execute(startRunnable);
    }
}
