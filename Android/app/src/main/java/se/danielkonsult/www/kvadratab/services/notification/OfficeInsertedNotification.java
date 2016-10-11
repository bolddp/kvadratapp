package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for a new office.
 */
public class OfficeInsertedNotification extends Notification {

    private static final String HEADER = "Nytt kontor";

    // Constructors

    public OfficeInsertedNotification() {
    }

    public OfficeInsertedNotification(int officeId, String name) {
        OfficeId = officeId;
        Name = name;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    // Public fields

    public int OfficeId;
    public String Name;
}
