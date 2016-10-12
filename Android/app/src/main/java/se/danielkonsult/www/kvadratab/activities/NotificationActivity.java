package se.danielkonsult.www.kvadratab.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.adapters.NotificationAdapter;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

public class NotificationActivity extends BaseActivity {

    private ListView _lvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setTitle("Händelser");

        _lvMain = (ListView) findViewById(R.id.lvMain);
        Notification[] notifications = AppCtrl.getDb().getNotificationRepository().getNotifications(Constants.NOTIFICATIONS_MAX_COUNT);

        _lvMain.setAdapter(new NotificationAdapter(this, notifications));
    }
}
