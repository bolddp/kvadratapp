package se.danielkonsult.www.kvadratab.services.notification;

/**
 * A notification about a consultant whose profile
 * picture has been updated.
 */
public class ConsultantUpdatedBitmapNotification extends Notification {

    private static final String HEADER = "Ny konsultbild";

    // Constructor

    public ConsultantUpdatedBitmapNotification() {
    }

    public ConsultantUpdatedBitmapNotification(int consultantId, String firstName, String lastName, String office) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        Office = office;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    // Fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
