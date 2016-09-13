package se.danielkonsult.www.kvadratab.repositories.office;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;

/**
 * Created by Daniel on 2016-09-11.
 */
public class DefaultOfficeDataRepository implements OfficeDataRepository {

    // Private variables

    private KvadratDb _db;

    // Constructor

    public DefaultOfficeDataRepository(KvadratDb db) {
        _db = db;
    }

    // Public methods

    @Override
    public void getById(final int id, final DbDataListener<OfficeData> listener) {
        AsyncTask<Void, Integer, OfficeData> task = new AsyncTask<Void, Integer, OfficeData>() {
            private String errorMessage = null;

            @Override
            protected OfficeData doInBackground(Void... params) {
                try {
                    String[] projection = {
                            DbSpec.OfficeEntry.COLUMN_NAME_ID,
                            DbSpec.OfficeEntry.COLUMN_NAME_NAME,
                    };
                    String selection = DbSpec.OfficeEntry.COLUMN_NAME_ID + " = ?";
                    String[] selectionArgs = {
                            Integer.toString(id)
                    };

                    SQLiteDatabase db = _db.getReadableDatabase();
                    Cursor c = db.query(DbSpec.OfficeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);
                    if (c.moveToFirst()){
                        OfficeData oData = new OfficeData();
                        oData.Id = c.getInt(c.getColumnIndex(DbSpec.OfficeEntry.COLUMN_NAME_ID));
                        oData.Name = c.getString(c.getColumnIndex(DbSpec.OfficeEntry.COLUMN_NAME_NAME));

                        return oData;
                    }

                    return null;
                }
                catch (Throwable ex){
                    errorMessage = ex.getMessage();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(OfficeData officeData) {
                if (!Utils.isStringNullOrEmpty(errorMessage))
                    listener.onError(errorMessage);
                else
                    listener.onResult(officeData);
            }
        };
        task.execute();
    }

    @Override
    public void getAll(final DbDataListener<OfficeData[]> listener) {
        AsyncTask<Void, Integer, OfficeData[]> task = new AsyncTask<Void, Integer, OfficeData[]>() {
            private String errorMessage;

            @Override
            protected OfficeData[] doInBackground(Void... params) {
                try {
                    String[] projection = {
                            DbSpec.OfficeEntry.COLUMN_NAME_ID,
                            DbSpec.OfficeEntry.COLUMN_NAME_NAME,
                    };

                    List<OfficeData> result = new ArrayList<>();

                    SQLiteDatabase db = _db.getReadableDatabase();
                    Cursor c = db.query(DbSpec.OfficeEntry.TABLE_NAME, projection, null, null, null, null, null);
                    while (c.moveToNext()){
                        OfficeData oData = new OfficeData();
                        oData.Id = c.getInt(c.getColumnIndex(DbSpec.OfficeEntry.COLUMN_NAME_ID));
                        oData.Name = c.getString(c.getColumnIndex(DbSpec.OfficeEntry.COLUMN_NAME_NAME));

                        result.add(oData);
                    }

                    return result.toArray(new OfficeData[result.size()]);
                }
                catch (Throwable ex){
                    errorMessage = ex.getMessage();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(OfficeData[] officeDatas) {
                if (officeDatas == null)
                    listener.onError(errorMessage);
                else
                    listener.onResult(officeDatas);
            }
        };
        task.execute();
    }

    @Override
    public void insert(final OfficeData office, final DbOperationListener listener) {
        // Setup the task to perform the insertion
        AsyncTask<Void, Integer, Long> task = new AsyncTask<Void, Integer, Long>() {

            private String errorMessage;

            @Override
            protected Long doInBackground(Void... params) {
                try {
                    SQLiteDatabase db = _db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DbSpec.OfficeEntry.COLUMN_NAME_ID, office.Id);
                    values.put(DbSpec.OfficeEntry.COLUMN_NAME_NAME, office.Name);

                    return db.insertOrThrow(DbSpec.OfficeEntry.TABLE_NAME, null, values);
                }
                catch (Throwable ex){
                    errorMessage = ex.getMessage();
                    return (long) -1;
                }
            }

            @Override
            protected void onPostExecute(Long id) {
                if (id >= -1)
                    listener.onResult(id);
                else
                    listener.onError(errorMessage);
            }
        };
        task.execute();
    }
}
