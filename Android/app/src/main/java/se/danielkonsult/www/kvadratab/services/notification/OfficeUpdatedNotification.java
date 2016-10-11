package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Created by Daniel on 2016-10-09.
 */
public class OfficeUpdatedNotification extends Notification {

    private static final String HEADER = "Kontor har nytt namn";

    // Constructors

    public OfficeUpdatedNotification() {
    }

    public OfficeUpdatedNotification(int id, String oldName, String newName) {
        Id = id;
        OldName = oldName;
        NewName = newName;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    // Fields

    public int Id;
    public String OldName;
    public String NewName;
}
