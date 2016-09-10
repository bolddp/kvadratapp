package se.danielkonsult.www.kvadratab.services.scraper;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Listener for data from a MainPageScraper.
 */
public interface ConsultantDataListener {
    void onResult(ConsultantData[] consultants);

    void onError(int statusCode, String message);
}
