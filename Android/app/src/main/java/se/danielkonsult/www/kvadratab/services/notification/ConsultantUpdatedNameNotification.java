package se.danielkonsult.www.kvadratab.services.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;

import se.danielkonsult.www.kvadratab.AppCtrl;

/**
 * Notification for when the name of a consultant has been changed.
 */
public class ConsultantUpdatedNameNotification extends Notification implements ClickableNotification {

    private static final String HEADER = "Namnbyte";

    // Constructors

    public ConsultantUpdatedNameNotification() {
    }

    public ConsultantUpdatedNameNotification(int consultantId, String oldFirstName, String oldLastName, String newFirstName, String newLastName, String office) {
        ConsultantId = consultantId;
        OldFirstName = oldFirstName;
        OldLastName = oldLastName;
        NewFirstName = newFirstName;
        NewLastName = newLastName;
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
        return String.format("%s %s, %s har bytt namn till <b>%s %s</b>", OldFirstName, OldLastName, Office, NewFirstName, NewLastName);
    }

    @Override
    public void click(Context context) {
        gotoConsultantDetailsActivity(context, ConsultantId);
    }

    // Fields

    public int ConsultantId;
    public String OldFirstName;
    public String OldLastName;
    public String NewFirstName;
    public String NewLastName;
    public String Office;
}
