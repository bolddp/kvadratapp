package se.danielkonsult.www.kvadratab.services.notification;

import android.graphics.Bitmap;

/**
 * Base class for notifications, containing common properties.
 */
public abstract class Notification {

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
