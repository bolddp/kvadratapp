package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Created by Daniel on 2016-10-09.
 */
public class OfficeChangedNotification extends Notification {

    // Constructors

    public OfficeChangedNotification() {
    }

    public OfficeChangedNotification(int id, String oldName, String newName) {
        Id = id;
        OldName = oldName;
        NewName = newName;
    }

    // Fields

    public int Id;
    public String OldName;
    public String NewName;
}
