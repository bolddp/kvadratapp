package se.danielkonsult.www.kvadratab.services.scraper;

/**
 * Created by Daniel on 2016-09-08.
 */
public class SummaryPageData {

    // Fields

    public TagData[] TagDatas;

    public OfficeData[] OfficeDatas;

    // Internal classes

    public class TagData {
        public int Id;
        public String Name;
    }

    public class OfficeData {
        public int Id;
        public String Name;
    }
}
