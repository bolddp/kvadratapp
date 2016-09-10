package se.danielkonsult.www.kvadratab.entities;

import se.danielkonsult.www.kvadratab.services.scraper.SummaryPageData;

/**
 * Carries information from the main web page about one consultant.
 */
public class ConsultantData {

    public int Id;

    public String Name;

    public OfficeData Office;

    public TagData[] Tags;
}
