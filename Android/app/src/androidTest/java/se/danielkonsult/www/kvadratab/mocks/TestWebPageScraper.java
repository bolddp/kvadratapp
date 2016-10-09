package se.danielkonsult.www.kvadratab.mocks;

import java.io.IOException;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.SummaryData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.scraper.WebPageScraper;

/**
 * Created by Daniel on 2016-10-09.
 */
public class TestWebPageScraper implements WebPageScraper {

    private SummaryData _summaryData;

    @Override
    public ConsultantData[] scrapeConsultants(int officeId, int tagId) throws IOException {
        return new ConsultantData[0];
    }

    @Override
    public SummaryData scrapeSummaryData() throws IOException {
        return _summaryData;
    }

    @Override
    public ConsultantDetails scrapeConsultantDetails(int consultantId) throws IOException {
        return null;
    }

    public void setSummaryData(OfficeData[] officeDatas, TagData[] tagDatas) {
        _summaryData = new SummaryData();
        _summaryData.OfficeDatas = officeDatas;
        _summaryData.TagDatas = tagDatas;
    }
}
