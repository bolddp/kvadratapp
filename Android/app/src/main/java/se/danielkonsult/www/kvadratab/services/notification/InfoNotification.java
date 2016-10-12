package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Created by Daniel on 2016-10-12.
 */
public class InfoNotification extends Notification {

    // Constructors

    public InfoNotification() {
    }

    public InfoNotification(String message) {
        Message = message;
    }

    @Override
    public String getHeader() {
        return "Information";
    }

    @Override
    public String getDetails() {
        return Message;
    }

    // Fields

    public String Message;
}
