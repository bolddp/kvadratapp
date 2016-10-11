package se.danielkonsult.www.kvadratab.services.notification;

import android.graphics.Bitmap;

import se.danielkonsult.www.kvadratab.AppCtrl;

/**
 * A notification about a new consultant that was found
 * during a refresh.
 */
public class ConsultantInsertedNotification extends Notification {

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

    // Public fields

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String Office;
}
