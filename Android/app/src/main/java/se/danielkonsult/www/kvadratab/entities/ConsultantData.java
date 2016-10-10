package se.danielkonsult.www.kvadratab.entities;

import android.graphics.Bitmap;

/**
 * Carries information from the main web page about one consultant.
 */
public class ConsultantData {

    // Constructors

    public ConsultantData() {
    }

    public ConsultantData(int id, String firstName, String lastName, int officeId) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        OfficeId = officeId;
    }

    public int Id;

    public String FirstName;
    public String LastName;
    public String JobRole;
    public String Description;
    public int OfficeId;
    public String Overview;

    /**
     * The timestamp when the details of the consultant
     * was last loaded. 0 = not previously loaded
     */
    public long DetailsTimstamp;

    // Relation properties

    public OfficeData Office;
    public TagData[] Tags;
    public String[] CompetenceAreas;
}
