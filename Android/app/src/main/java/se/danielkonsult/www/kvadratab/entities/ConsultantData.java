package se.danielkonsult.www.kvadratab.entities;

/**
 * Carries information from the main web page about one consultant.
 */
public class ConsultantData {

    public int Id;

    public String Name;
    public String JobRole;
    public String Description;

    public int OfficeId;
    public OfficeData Office;

    public TagData[] Tags;
}
