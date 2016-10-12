package se.danielkonsult.www.kvadratab.services.notification;

import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;

/**
 * Created by Daniel on 2016-10-05.
 */
public class DefaultNotificationService implements NotificationService {

    @Override
    public void add(Notification notification) {
        // Add to database
        AppCtrl.getDb().getNotificationRepository().insert(notification);
    }

    @Override
    public void addAll(List<Notification> notifications) {
        // Add to database
        for (Notification notification : notifications){
            AppCtrl.getDb().getNotificationRepository().insert(notification);
        }
    }
}
