package se.danielkonsult.www.kvadratab.repositories.notification;

import se.danielkonsult.www.kvadratab.entities.NotificationData;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Created by Daniel on 2016-10-05.
 */
public interface NotificationRepository {

    /**
     * Gets all or a specified maximum count of notification data entities from the database.
     * @param maxCount The maximum number of notifications to return. 0 = return all.
     */
    Notification[] getNotifications(int maxCount);

    /**
     * Inserts a new notification in the database.
     * @param notification
     */
    void insert(Notification notification);

    /**
     * Deletes all notifications.
     */
    void clear();
}
