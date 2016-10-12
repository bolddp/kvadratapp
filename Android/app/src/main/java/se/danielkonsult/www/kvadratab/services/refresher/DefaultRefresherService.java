package se.danielkonsult.www.kvadratab.services.refresher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Responsible for refreshing the data in the database, by perform a new scraping
 * of the web page and comparing the results with the existing data.
 * Also responsible for checking whether enough time has passed
 * since the last refresh was performed.
 */
public class DefaultRefresherService extends BroadcastReceiver implements RefresherService {

    // Private variables

    private static final String TAG = "DefaultRefresherService";

    // Private methods

    private void performRefresh(){
        try {
            List<Notification> notifications = new ArrayList<>();

            // Compare offices
            notifications.addAll(OfficeComparer.compare());
            notifications.addAll(ConsultantComparer.compare());

            // Send all notifications to the service
            AppCtrl.getNotificationService().addAll(notifications, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void ensureStarted() {
        AlarmManager alarmManager = (AlarmManager) AppCtrl.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        long milliSecondsPerDay = 1000 * 60 * 60 * 24;

        Random rnd = new Random();
        int hour = rnd.nextInt(2) + 8;
        int minutes = rnd.nextInt(60);

        // The alarm that triggers a refresh should go off some time between 8:00 AM and 9:59 AM
        // to avoid having app instances choking the web page at the same time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);

        Log.d(TAG, String.format("Set to go off at %2d:%2d", hour, minutes));

        Intent refresherIntent = new Intent(AppCtrl.getApplicationContext(), DefaultRefresherService.class);
        refresherIntent.setAction(Constants.REFRESHER_INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AppCtrl.getApplicationContext(), Constants.REFRESHER_INTENT_REQUEST_CODE, refresherIntent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                milliSecondsPerDay, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure we have a context to work with
        if (AppCtrl.getApplicationContext() == null)
            AppCtrl.setApplicationContext(context);

        Log.d(TAG, "onReceive! action: " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ensureStarted();
        }
        else if (intent.getAction().equals(Constants.REFRESHER_INTENT_ACTION)) {
            performRefresh();
        }
    }
}
