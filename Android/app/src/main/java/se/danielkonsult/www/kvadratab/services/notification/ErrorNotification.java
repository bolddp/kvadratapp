package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Created by Daniel on 2016-10-12.
 */
public class ErrorNotification extends Notification {

    // Constructor

    public ErrorNotification() {
    }

    public ErrorNotification(String message) {
        Message = message;
    }

    @Override
    public String getHeader() {
        return "Det uppstod ett fel!";
    }

    @Override
    public String getDetails() {
        return Message;
    }

    // Fields

    public String Message;
}
