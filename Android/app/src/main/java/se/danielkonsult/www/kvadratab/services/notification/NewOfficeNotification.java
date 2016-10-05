package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Created by Daniel on 2016-10-05.
 */
public class NewOfficeNotification extends Notification {

    // Constructors

    public NewOfficeNotification() {
    }

    public NewOfficeNotification(int officeId, String name) {
        OfficeId = officeId;
        Name = name;
    }

    // Public fields

    public int OfficeId;
    public String Name;
}
