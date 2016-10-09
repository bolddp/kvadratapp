package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for a removed office.
 */
public class OfficeRemovedNotification extends Notification {

    // Constructor

    public OfficeRemovedNotification() {
    }

    public OfficeRemovedNotification(int id, String name) {
        Id = id;
        Name = name;
    }

    // Fields

    public int Id;
    public String Name;
}
