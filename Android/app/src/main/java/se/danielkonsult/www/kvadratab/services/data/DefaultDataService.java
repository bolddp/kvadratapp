package se.danielkonsult.www.kvadratab.services.data;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.scraper.ImageHelper;
import se.danielkonsult.www.kvadratab.helpers.scraper.WebPageScraper;

/**
 * Created by Daniel on 2016-09-14.
 */
public class DefaultDataService implements DataService {

    // Private variables

    private final DataServiceListeners _listeners = new DataServiceListeners();

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
                    InitialLoader loader = new InitialLoader(db, _listeners);
                    loader.run();
                }
                else {
                    // Signal that the data already is available, but also
                    // check if it's time for a refresh of the data.
                    _listeners.onLoaded();

                    Refresher refresher = new Refresher(db, _listeners);
                    refresher.run();
                }
            }
        };
        AsyncTask.execute(startRunnable);
    }
}
