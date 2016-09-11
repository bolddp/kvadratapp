package se.danielkonsult.www.kvadratab.services.scraper;

/**
 * Listener for data from a MainPageScraper.
 */
public interface SummaryDataListener {
    void onResult(SummaryData data);

    void onError(int statusCode, String message);
}
