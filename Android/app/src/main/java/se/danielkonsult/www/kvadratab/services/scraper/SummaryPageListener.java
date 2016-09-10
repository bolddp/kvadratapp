package se.danielkonsult.www.kvadratab.services.scraper;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Listener for data from a MainPageScraper.
 */
public interface SummaryPageListener {
    void onResult(SummaryPageData data);

    void onError(int statusCode, String message);
}
