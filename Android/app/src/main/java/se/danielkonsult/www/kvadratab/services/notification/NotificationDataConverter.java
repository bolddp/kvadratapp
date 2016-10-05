package se.danielkonsult.www.kvadratab.services.notification;

import com.google.gson.Gson;

import se.danielkonsult.www.kvadratab.entities.NotificationData;

/**
 * Converts between NotificationData and Notification descendants.
 */
public class NotificationDataConverter {

    // Private methods

    private static final Gson _gson = new Gson();
    private static String _packageName = NotificationDataConverter.class.getPackage().getName();

    // Static methods

    public static Notification toNotification(NotificationData notificationData) {
        try {
            Class<?> clss = Class.forName(String.format("%s.%s", _packageName, notificationData.Type));
            Notification result = (Notification) _gson.fromJson(notificationData.Data, clss);
            result.Timestamp = notificationData.Timestamp;

            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NotificationData toNotificationData(Notification notification) {
        NotificationData nd = new NotificationData();
        nd.Timestamp = notification.Timestamp;
        nd.Type = notification.getClass().getSimpleName();
        nd.Data = _gson.toJson(notification);

        return nd;
    }
}
