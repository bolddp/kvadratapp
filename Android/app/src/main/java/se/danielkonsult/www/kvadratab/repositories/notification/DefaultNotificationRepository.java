package se.danielkonsult.www.kvadratab.repositories.notification;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.NotificationData;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.notification.Notification;
import se.danielkonsult.www.kvadratab.services.notification.NotificationDataConverter;

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
    public Notification[] getNotifications(int maxCount) {

        // Read out the relevant NotificationData instances
        List<Notification> result = new ArrayList<>();
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.NotificationEntry.TABLE_NAME, queryProjection, null, null, null, null, orderBy);
        while (c.moveToNext() && ((maxCount == 0) || (result.size() < maxCount))) {
            NotificationData nd = getFromCursor(c);
            Notification notification = NotificationDataConverter.toNotification(nd);

            result.add(notification);
        }

        return result.toArray(new Notification[result.size()]);
    }

    @Override
    public void insert(Notification notification) {
        NotificationData nd = NotificationDataConverter.toNotificationData(notification);

        SQLiteDatabase db = _db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSpec.NotificationEntry.COLUMN_NAME_ID, nd.Id);
        values.put(DbSpec.NotificationEntry.COLUMN_NAME_TIMESTAMP, nd.Timestamp);
        values.put(DbSpec.NotificationEntry.COLUMN_NAME_TYPE, nd.Type);
        values.put(DbSpec.NotificationEntry.COLUMN_NAME_DATA, nd.Data);

        db.insertOrThrow(DbSpec.NotificationEntry.TABLE_NAME, null, values);
    }
}
