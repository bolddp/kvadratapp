package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for a new office.
 */
public class OfficeNewNotification extends Notification {

    // Constructors

    public OfficeNewNotification() {
    }

    public OfficeNewNotification(int officeId, String name) {
        OfficeId = officeId;
        Name = name;
    }

    // Public fields

    public int OfficeId;
    public String Name;
}
