package se.danielkonsult.www.kvadratab.mocks;

import java.io.IOException;
import java.util.HashMap;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.SummaryData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.scraper.WebPageScraper;

/**
 * Web scraper for unit tests, that can be setup with arbitrary data instead
 * of actually scraping it from the web.
 */
public class TestWebPageScraper implements WebPageScraper {

    private SummaryData _summaryData;
    private final HashMap<Integer, ConsultantData[]> _officeConsultantDatas = new HashMap<>();

    @Override
    public ConsultantData[] scrapeConsultants(int officeId, int tagId) throws IOException {
        if (!_officeConsultantDatas.containsKey(officeId))
            return new ConsultantData[0];

        return _officeConsultantDatas.get(officeId);
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

    public void setConsultantData(int officeId, ConsultantData[] consultantDatas) {
        _officeConsultantDatas.put(officeId, consultantDatas);
    }
}
