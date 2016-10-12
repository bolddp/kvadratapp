package se.danielkonsult.www.kvadratab.services.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.List;
import java.util.StringTokenizer;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.activities.NotificationActivity;

/**
 * Handles notifications that inform the user of new or updated data
 * in the app, e.g. a new consultant.
 */
public class DefaultNotificationService implements NotificationService {

    private static final int NOTIFICATION_ID = 11;

    // Private methods

    private void createNotification(int count) {
        String contentText = (count > 1)
                ? String.format("%d nya händelser", count)
                : String.format("%d ny händelse", count);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(AppCtrl.getApplicationContext())
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(AppCtrl.getApplicationContext().getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(AppCtrl.getApplicationContext().getString(R.string.app_name))
                        .setContentText(contentText);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(AppCtrl.getApplicationContext(), NotificationActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppCtrl.getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) AppCtrl.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    @Override
    public void add(Notification notification, boolean createNotification) {
        // Add to database
        AppCtrl.getDb().getNotificationRepository().insert(notification);
        if (createNotification) {
            createNotification(1);
        }

    }

    @Override
    public void addAll(List<Notification> notifications, boolean createNotification) {
        // Add to database
        for (Notification notification : notifications){
            AppCtrl.getDb().getNotificationRepository().insert(notification);
        }
        if (createNotification) {
            createNotification(notifications.size());
        }
    }
}
