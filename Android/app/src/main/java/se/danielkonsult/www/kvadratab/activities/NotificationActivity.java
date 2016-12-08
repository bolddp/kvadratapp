package se.danielkonsult.www.kvadratab.activities;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.adapters.NotificationAdapter;
import se.danielkonsult.www.kvadratab.helpers.Constants;
import se.danielkonsult.www.kvadratab.helpers.Dialogs;
import se.danielkonsult.www.kvadratab.services.notification.ClickableNotification;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

public class NotificationActivity extends BaseActivity {

    private ListView _lvMain;
    private Notification[] _notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setTitle("Händelser");

        _lvMain = (ListView) findViewById(R.id.lvMain);
        _lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Is it a clickable notification?
                if (_notifications[position] instanceof ClickableNotification) {
                    ((ClickableNotification) _notifications[position]).click(NotificationActivity.this);
                }
            }
        });
        _notifications = AppCtrl.getDb().getNotificationRepository().getNotifications(Constants.NOTIFICATIONS_MAX_COUNT);

        _lvMain.setAdapter(new NotificationAdapter(this, _notifications));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_clear_notifications) {
            // Confirm it
            Dialogs.displayYesNo(this, R.string.question_header, R.string.msg_confirm_clear_notifications, R.string.yes, R.string.no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AppCtrl.getDb().getNotificationRepository().clear();
                            // No point in hanging around this activity...
                            finish();
                        }
                    }, null);
        }

        return super.onOptionsItemSelected(item);
    }
}
