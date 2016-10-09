package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for a new office.
 */
public class OfficeInsertedNotification extends Notification {

    // Constructors

    public OfficeInsertedNotification() {
    }

    public OfficeInsertedNotification(int officeId, String name) {
        OfficeId = officeId;
        Name = name;
    }

    // Public fields

    public int OfficeId;
    public String Name;
}
