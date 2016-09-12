package se.danielkonsult.www.kvadratab.repositories.tag;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.repositories.office.OfficeDataRepository;

/**
 * Created by Daniel on 2016-09-11.
 */
public class DefaultTagDataRepository implements TagDataRepository {

    // Private variables

    private KvadratDb _db;

    // Constructor

    public DefaultTagDataRepository(KvadratDb db) {
        _db = db;
    }

    // Public methods

    @Override
    public void getById(final int id, final DbDataListener<TagData> listener) {
        AsyncTask<Void, Integer, TagData> task = new AsyncTask<Void, Integer, TagData>() {
            private String errorMessage = null;

            @Override
            protected TagData doInBackground(Void... params) {
                try {
                    String[] projection = {
                            DbSpec.TagEntry.COLUMN_NAME_ID,
                            DbSpec.TagEntry.COLUMN_NAME_NAME,
                    };
                    String selection = DbSpec.TagEntry.COLUMN_NAME_ID + " = ?";
                    String[] selectionArgs = {
                            Integer.toString(id)
                    };

                    SQLiteDatabase db = _db.getReadableDatabase();
                    Cursor c = db.query(DbSpec.TagEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);
                    if (c.moveToFirst()){
                        TagData oData = new TagData();
                        oData.Id = c.getInt(c.getColumnIndex(DbSpec.TagEntry.COLUMN_NAME_ID));
                        oData.Name = c.getString(c.getColumnIndex(DbSpec.TagEntry.COLUMN_NAME_NAME));

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
            protected void onPostExecute(TagData TagData) {
                if (!Utils.isStringNullOrEmpty(errorMessage))
                    listener.onError(errorMessage);
                else
                    listener.onResult(TagData);
            }
        };
        task.execute();
    }

    @Override
    public void getAll(final DbDataListener<TagData[]> listener) {
        AsyncTask<Void, Integer, TagData[]> task = new AsyncTask<Void, Integer, TagData[]>() {
            private String errorMessage;

            @Override
            protected TagData[] doInBackground(Void... params) {
                try {
                    String[] projection = {
                            DbSpec.TagEntry.COLUMN_NAME_ID,
                            DbSpec.TagEntry.COLUMN_NAME_NAME,
                    };

                    List<TagData> result = new ArrayList<>();

                    SQLiteDatabase db = _db.getReadableDatabase();
                    Cursor c = db.query(DbSpec.TagEntry.TABLE_NAME, projection, null, null, null, null, null);
                    while (c.moveToNext()){
                        TagData tag = new TagData();
                        tag.Id = c.getInt(c.getColumnIndex(DbSpec.TagEntry.COLUMN_NAME_ID));
                        tag.Name = c.getString(c.getColumnIndex(DbSpec.TagEntry.COLUMN_NAME_NAME));

                        result.add(tag);
                    }

                    return result.toArray(new TagData[result.size()]);
                }
                catch (Throwable ex){
                    errorMessage = ex.getMessage();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(TagData[] TagDatas) {
                if (TagDatas == null)
                    listener.onError(errorMessage);
                else
                    listener.onResult(TagDatas);
            }
        };
        task.execute();
    }

    @Override
    public void insert(final TagData tag, final DbOperationListener listener) {
        // Setup the task to perform the insertion
        AsyncTask<Void, Integer, Long> task = new AsyncTask<Void, Integer, Long>() {

            private String errorMessage;

            @Override
            protected Long doInBackground(Void... params) {
                try {
                    SQLiteDatabase db = _db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DbSpec.TagEntry.COLUMN_NAME_ID, tag.Id);
                    values.put(DbSpec.TagEntry.COLUMN_NAME_NAME, tag.Name);

                    return db.insert(DbSpec.TagEntry.TABLE_NAME, null, values);
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
