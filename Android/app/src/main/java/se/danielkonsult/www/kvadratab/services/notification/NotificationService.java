package se.danielkonsult.www.kvadratab.services.notification;

import java.util.List;

/**
 * Created by Daniel on 2016-10-05.
 */
public interface NotificationService {

    void add(Notification notification, boolean createNotification);

    void addAll(List<Notification> notifications, boolean createNotification);
}
