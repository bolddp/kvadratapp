package se.danielkonsult.www.kvadratab.services.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.activities.ConsultantDetailsActivity;
import se.danielkonsult.www.kvadratab.helpers.Constants;

/**
 * A notification about a new consultant that was found
 * during a refresh.
 */
public class ConsultantInsertedNotification extends Notification implements ClickableNotification {

    private static final String HEADER = "Ny konsult";

    // Constructors

    public ConsultantInsertedNotification() {
    }

    public ConsultantInsertedNotification(int consultantId, String firstName, String lastName, String office) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        Office = office;
    }

    @Override
    public Bitmap getBitmap() {
        return AppCtrl.getImageService().getConsultantBitmapFromFile(ConsultantId);
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String getDetails() {
        return String.format("<b>%s %s, %s</b> är tillagd som konsult", FirstName, LastName, Office);
    }

    @Override
    public void click(Context context) {
        gotoConsultantDetailsActivity(context, ConsultantId);
    }

    // Public fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
