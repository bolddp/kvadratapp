package se.danielkonsult.www.kvadratab.services.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import se.danielkonsult.www.kvadratab.activities.ConsultantDetailsActivity;
import se.danielkonsult.www.kvadratab.helpers.Constants;

/**
 * Base class for notifications, containing common properties.
 */
public abstract class Notification {

    // Protected methods

    protected void gotoConsultantDetailsActivity(Context context, int consultantId) {
        Intent intent = new Intent(context, ConsultantDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_CONSULTANT_ID, consultantId);
        context.startActivity(intent);
    }

    // Constructor

    public Notification() {
        Timestamp = System.currentTimeMillis();
    }

    /**
     * Gets a string that should be used as the header of the notification,
     * e.g. "Ny konsult".
     */
    public abstract String getHeader();

    public String getDetails(){
        return null;
    }

    public Bitmap getBitmap() {
        return null;
    }

    // Fields

    public long Timestamp;
}
