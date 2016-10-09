package se.danielkonsult.www.kvadratab.services.notification;

/**
 * A notification about a new consultant that was found
 * during a refresh.
 */
public class ConsultantInsertedNotification extends Notification {

    // Constructors

    public ConsultantInsertedNotification() {
    }

    public ConsultantInsertedNotification(int consultantId, String firstName, String lastName, String office) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        Office = office;
    }

    // Public fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
