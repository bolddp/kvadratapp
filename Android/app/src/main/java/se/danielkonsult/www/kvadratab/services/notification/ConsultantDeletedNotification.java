package se.danielkonsult.www.kvadratab.services.notification;

/**
 * A notification for a consultant that has been removed
 * from the web page.
 */
public class ConsultantDeletedNotification extends Notification {

    private static final String HEADER = "Borttagen konsult";

    // Constructors

    public ConsultantDeletedNotification() {
    }

    public ConsultantDeletedNotification(int consultantId, String firstName, String lastName, String office) {
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
