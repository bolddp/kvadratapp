package se.danielkonsult.www.kvadratab.mocks;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.services.notification.Notification;
import se.danielkonsult.www.kvadratab.services.notification.NotificationService;

/**
 * Created by Daniel on 2016-10-09.
 */
public class TestNotificationService implements NotificationService {

    // Private variables

    private List<Notification> _notifications = new ArrayList<>();

    @Override
    public void addNotification(Notification notification) {
        _notifications.add(notification);
    }

    public List<Notification> getNotifications(){
        return _notifications;
    }
}
