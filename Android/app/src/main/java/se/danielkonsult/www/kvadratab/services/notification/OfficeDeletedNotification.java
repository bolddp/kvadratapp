package se.danielkonsult.www.kvadratab.services.notification;

import android.text.Html;

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

    @Override
    public String getDetails() {
        return String.format("Kontor <b>%s</b> har tagits bort", Name);
    }

    // Fields

    public int Id;
    public String Name;
}
