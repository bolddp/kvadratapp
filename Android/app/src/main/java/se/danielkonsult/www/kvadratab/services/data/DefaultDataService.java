package se.danielkonsult.www.kvadratab.services.data;

import android.os.AsyncTask;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.scraper.ImageHelper;

/**
 * Created by Daniel on 2016-09-14.
 */
public class DefaultDataService implements DataService {

    // Private variables

    private final DataServiceListeners _listeners = new DataServiceListeners();
    private static ConsultantData[] _consultantCache;

    /**
     * Loads the consultant images from disk and attaches them to the consultants.
     */
//    private void loadConsultantImages(ConsultantData[] consultants) {
//        for (ConsultantData cd : consultants){
//            cd.Image = ImageHelper.getConsultantBitmapFromFile(cd.Id);
//        }
//    }

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
                    // Prep by loading the consultants before saying we're finished
                   setConsultants(db.getAllConsultants(true));

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

    @Override
    public void setConsultants(ConsultantData[] consultants) {
        // Not viable, causes Out of memory error
        // loadConsultantImages(consultants);

        _consultantCache = consultants;
    }

    @Override
    public ConsultantData[] getConsultants() {
        if (_consultantCache == null)
            _consultantCache = AppCtrl.getDb().getAllConsultants(true);
        return _consultantCache;
    }
}
