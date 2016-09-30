package se.danielkonsult.www.kvadratab.services.refresher;

import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Responsible for refreshing the data in the database, by perform a new scraping
 * of the web page and comparing the results with the existing data.
 * Also responsible for checking whether enough time has passed
 * since the last refresh was performed.
 */
public class DefaultRefresherService implements RefresherService {

    @Override
    public void ensureStarted() {

    }
}
