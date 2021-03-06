package se.danielkonsult.www.kvadratab.helpers.scraper;

import java.io.IOException;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.entities.SummaryData;

/**
 * Created by Daniel on 2016-10-09.
 */
public interface WebPageScraper {
    ConsultantData[] scrapeConsultants(int officeId, int tagId) throws IOException;

    SummaryData scrapeSummaryData() throws IOException;

    ConsultantDetails scrapeConsultantDetails(int consultantId) throws IOException;
}
