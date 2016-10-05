package se.danielkonsult.www.kvadratab.services.notification;

/**
 * A notification about a new consultant that was found
 * during a refresh.
 */
public class NewConsultantNotification extends Notification {

    // Constructors

    public NewConsultantNotification() {
    }

    public NewConsultantNotification(int consultantId, String name, String office) {
        ConsultantId = consultantId;
        Name = name;
        Office = office;
    }

    // Public fields

    public int ConsultantId;
    public String Name;
    public String Office;
}
