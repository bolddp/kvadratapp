package se.danielkonsult.www.kvadratab.repositories.notification;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.NotificationData;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Created by Daniel on 2016-10-05.
 */
public class DefaultNotificationRepository implements NotificationRepository {

    // Private variables

    private final String[] queryProjection = {
            DbSpec.NotificationEntry.COLUMN_NAME_ID,
            DbSpec.NotificationEntry.COLUMN_NAME_TIMESTAMP,
            DbSpec.NotificationEntry.COLUMN_NAME_TYPE,
            DbSpec.NotificationEntry.COLUMN_NAME_DATA
    };
    private final String orderBy = DbSpec.NotificationEntry.COLUMN_NAME_TIMESTAMP + " DESC";

    private KvadratDb _db;

    // Private methods

    private NotificationData getFromCursor(Cursor c) {
        String type = c.getString(c.getColumnIndex(DbSpec.NotificationEntry.COLUMN_NAME_TYPE));

        NotificationData notification = new NotificationData();
        notification.Id = c.getInt(c.getColumnIndex(DbSpec.NotificationEntry.COLUMN_NAME_ID));
        notification.Timestamp = c.getLong(c.getColumnIndex(DbSpec.NotificationEntry.COLUMN_NAME_TIMESTAMP));
        notification.Type = c.getString(c.getColumnIndex(DbSpec.NotificationEntry.COLUMN_NAME_TYPE));
        notification.Data = c.getString(c.getColumnIndex(DbSpec.NotificationEntry.COLUMN_NAME_DATA));

        return notification;
    }

    // Constructor

    public DefaultNotificationRepository(KvadratDb db) {
        this._db = db;
    }

    @Override
    public NotificationData[] getNotifications(int maxCount) {
        List<NotificationData> result = new ArrayList<>();

        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.NotificationEntry.TABLE_NAME, queryProjection, null, null, null, null, orderBy);
        while (c.moveToNext() && ((maxCount == 0) || (result.size() < maxCount))) {
            result.add(getFromCursor(c));
        }

        return result.toArray(new NotificationData[result.size()]);
    }
}
