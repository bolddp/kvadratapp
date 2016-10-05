package se.danielkonsult.www.kvadratab.entities;

/**
 * Represents a notification in a format that can be stored in the database.
 */
public class NotificationData {

    // Constructor

    public NotificationData() {
    }

    // Public fields

    public int Id;
    public long Timestamp;
    public String Type;
    public String Data;
}
