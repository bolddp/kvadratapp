package se.danielkonsult.www.kvadratab.services.notification;

/**
 * A notification about a consultant whose profile
 * picture has been updated.
 */
public class ConsultantUpdatedBitmapNotification extends Notification {

    // Constructor

    public ConsultantUpdatedBitmapNotification() {
    }

    public ConsultantUpdatedBitmapNotification(int consultantId, String firstName, String lastName, String office) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        Office = office;
    }

    // Fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
