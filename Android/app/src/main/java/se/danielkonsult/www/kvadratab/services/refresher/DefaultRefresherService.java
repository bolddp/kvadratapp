package se.danielkonsult.www.kvadratab.services.refresher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Responsible for refreshing the data in the database, by perform a new scraping
 * of the web page and comparing the results with the existing data.
 * Also responsible for checking whether enough time has passed
 * since the last refresh was performed.
 */
public class DefaultRefresherService implements RefresherService {

    // Private variables

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            performRefresh();
        }
    };

    // Private methods

    private void performRefresh(){
        // TBD
    }

    @Override
    public void ensureStarted() {
        // TBD
    }
}
