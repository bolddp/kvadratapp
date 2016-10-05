package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Base class for notifications, containing common properties.
 */
public abstract class Notification {

    // Constructor

    public Notification() {
        Timestamp = System.currentTimeMillis();
    }

    // Fields

    public long Timestamp;
}
