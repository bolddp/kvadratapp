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

    /**
     * Performs the initial load of data by scraping the web page and
     * storing the found data in the database.
     */
    private void performInitialLoad() {

        final String MODULE = "InitialLoad";

        KvadratDb db = AppCtrl.getDb();

        try {
            // Notify listeners that the initial load has started
            _listeners.onInitialLoadStarted();

            int progress = 0;

            // Scrape the web page for all consultants and loop them
            ConsultantData[] consultants = WebPageScraper.scrapeConsultants(0,0);
            _listeners.onInitialLoadProgress(progress, consultants.length);
            for (ConsultantData cd : consultants){
                // Save the consultant to database
                db.insertConsultant(cd);
                // Load the consultant image
                Bitmap bitmap = ImageHelper.downloadConsultantBitmapAndSaveToFile(cd.Id);


                progress++;
                _listeners.onInitialLoadProgress(progress, consultants.length);
            }

            // One final progress notification...
            _listeners.onInitialLoadProgress(consultants.length, consultants.length);
        }
        catch (Throwable ex){
            // Notify listeners of the problem
            _listeners.onError(MODULE, ex.getMessage());
        }

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
