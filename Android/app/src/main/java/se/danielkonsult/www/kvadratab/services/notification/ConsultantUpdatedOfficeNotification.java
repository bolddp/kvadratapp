package se.danielkonsult.www.kvadratab.services.notification;

import android.content.Context;
import android.text.Html;

/**
 * Created by Daniel on 2016-10-10.
 */
public class ConsultantUpdatedOfficeNotification extends Notification implements ClickableNotification {

    private static final String HEADER = "Konsult har bytt kontor";

    // Constructor


    public ConsultantUpdatedOfficeNotification() {
    }

    public ConsultantUpdatedOfficeNotification(int consultantId, String firstName, String lastName, String newOffice) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        NewOffice = newOffice;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String getDetails() {
        return String.format("<b>%s %s</b> bytt kontor till <b>%s</b>", FirstName, LastName, NewOffice);
    }

    @Override
    public void click(Context context) {
        gotoConsultantDetailsActivity(context, ConsultantId);
    }

    // Fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String NewOffice;
}
