package se.danielkonsult.www.kvadratab;

import se.danielkonsult.www.kvadratab.services.scraper.DefaultScraperService;
import se.danielkonsult.www.kvadratab.services.scraper.ScraperService;

/**
 * Application-wide singleton with a responsibility to provide services
 * and data to the rest of the application.
 */
public class AppCtrl {

    // Fields

    private static DefaultScraperService _scraperService;

    // Public methods

    public static ScraperService getScraperService(){
        if (_scraperService == null)
            _scraperService = new DefaultScraperService();

        return _scraperService;
    }
}
