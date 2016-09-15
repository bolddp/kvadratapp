package se.danielkonsult.www.kvadratab.entities;

import android.graphics.Bitmap;

/**
 * Carries information from the main web page about one consultant.
 */
public class ConsultantData {

    public int Id;

    public String FirstName;
    public String LastName;
    public String JobRole;
    public String Description;

    public int OfficeId;
    public OfficeData Office;

    public TagData[] Tags;

    /**
     * The image of the consultant, linked at runtime and
     * not stored in the database but in separate file.
     */
    // public Bitmap Image;
}
