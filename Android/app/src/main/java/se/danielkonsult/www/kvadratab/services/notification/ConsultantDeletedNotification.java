package se.danielkonsult.www.kvadratab.services.notification;

import android.text.Html;

/**
 * A notification for a consultant that has been removed
 * from the web page.
 */
public class ConsultantDeletedNotification extends Notification {

    private static final String HEADER = "Borttagen konsult";

    // Constructors

    public ConsultantDeletedNotification() {
    }

    public ConsultantDeletedNotification(int consultantId, String firstName, String lastName, String office) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        Office = office;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String getDetails() {
        return String.format("<b>%s %s, %s</b> har tagits bort ur konsultregistret", FirstName, LastName, Office);
    }

    // Fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
