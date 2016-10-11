package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Notification for a removed office.
 */
public class OfficeDeletedNotification extends Notification {

    private static final String HEADER = "Kontor har tagits bort";

    // Constructor

    public OfficeDeletedNotification() {
    }

    public OfficeDeletedNotification(int id, String name) {
        Id = id;
        Name = name;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    // Fields

    public int Id;
    public String Name;
}
