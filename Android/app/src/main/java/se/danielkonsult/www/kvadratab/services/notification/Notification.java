package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Base class for notifications, containing common properties.
 */
public abstract class Notification {

    // Constructor

    public Notification() {
        TimestampMillis = System.currentTimeMillis();
    }

    // Fields

    public final long TimestampMillis;
}
