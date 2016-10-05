package se.danielkonsult.www.kvadratab.services.notification;

/**
 * A notification about a new consultant that was found
 * during a refresh.
 */
public class NewConsultantNotification extends Notification {

    // Constructors

    public NewConsultantNotification() {
    }

    public NewConsultantNotification(int id, String name, String office) {
        Id = id;
        Name = name;
        Office = office;
    }

    // Public fields

    public int Id;
    public String Name;
    public String Office;
}
