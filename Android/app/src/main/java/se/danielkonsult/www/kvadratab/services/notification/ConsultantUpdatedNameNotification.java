package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for when the name of a consultant has been changed.
 */
public class ConsultantUpdatedNameNotification extends Notification {

    private static final String HEADER = "Namnbyte";

    // Constructors

    public ConsultantUpdatedNameNotification() {
    }

    public ConsultantUpdatedNameNotification(int consultantId, String oldFirstName, String oldLastName, String newFirstName, String newLastName) {
        ConsultantId = consultantId;
        OldFirstName = oldFirstName;
        OldLastName = oldLastName;
        NewFirstName = newFirstName;
        NewLastName = newLastName;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    // Fields

    public int ConsultantId;
    public String OldFirstName;
    public String OldLastName;
    public String NewFirstName;
    public String NewLastName;
}
