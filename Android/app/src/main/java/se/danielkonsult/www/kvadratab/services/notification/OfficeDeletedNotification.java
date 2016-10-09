package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for a removed office.
 */
public class OfficeDeletedNotification extends Notification {

    // Constructor

    public OfficeDeletedNotification() {
    }

    public OfficeDeletedNotification(int id, String name) {
        Id = id;
        Name = name;
    }

    // Fields

    public int Id;
    public String Name;
}
