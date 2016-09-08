package se.danielkonsult.www.kvadratab.services.scraper;

import se.danielkonsult.www.kvadratab.entities.MainPageWebData;

/**
 * Listener for data from a MainPageScraper.
 */
public interface MainPageScraperListener {
    void onResult(MainPageWebData[] webDatas);

    void onError(int statusCode, String message);
}
