package se.danielkonsult.www.kvadratab.services.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;

import se.danielkonsult.www.kvadratab.AppCtrl;

/**
 * A notification about a consultant whose profile
 * picture has been updated.
 */
public class ConsultantUpdatedBitmapNotification extends Notification implements ClickableNotification {

    private static final String HEADER = "Ny profilbild";

    // Constructor

    public ConsultantUpdatedBitmapNotification() {
    }

    public ConsultantUpdatedBitmapNotification(int consultantId, String firstName, String lastName, String office) {
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
        return String.format("<b>%s %s, %s</b> har uppdaterat sin profilbild", FirstName, LastName, Office);
    }

    @Override
    public void click(Context context) {
        gotoConsultantDetailsActivity(context, ConsultantId);
    }

    // Fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
