package se.danielkonsult.www.kvadratab.helpers.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.OfficeData;

/**
 * Handles the database that stores data that is downloaded from the
 * Kvadrat web page.
 */
public class KvadratDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kvadrat.db";

    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static class ConsultantEntry implements BaseColumns {
        public static final String TABLE_NAME = "consultant";
        public static final String COLUMN_NAME_ID = "webid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_OFFICEID = "office_id";
        public static final String COLUMN_NAME_JOBROLE = "jobrole";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    private static class OfficeEntity implements BaseColumns {
        public static final String COLUMN_NAME_ID = "webid";
        public static final String COLUMN_NAME_NAME = "name";
    }

    private static final String SQL_CREATE_CONSULTANTS =
            "CREATE TABLE " + ConsultantEntry.TABLE_NAME + " (" +
                    ConsultantEntry._ID + " INTEGER PRIMARY KEY," +
                    ConsultantEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                    ConsultantEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ConsultantEntry.COLUMN_NAME_JOBROLE + TEXT_TYPE + COMMA_SEP +
                    ConsultantEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + " )";

    protected static final String SQL_DELETE_CONSULTANTS =
            "DROP TABLE IF EXISTS " + ConsultantEntry.TABLE_NAME;

    // Protected methods

    /**
     * Drops the database by erasing all its contents.
     */
    protected void drop(SQLiteDatabase db){
        // No code here, database should not be droppable
    }
    // Constructor

    public KvadratDb() {
        super(AppCtrl.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected KvadratDb(Context context, String databaseName){
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONSULTANTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONSULTANTS);
        onCreate(db);
    }

    // Database operation methods

    public void insertOffice(final OfficeData office, final DbOperationListener listener) {
        // Setup the task to perform the insertion
        AsyncTask<Void, Integer, Long> task = new AsyncTask<Void, Integer, Long>() {

            private String errorMessage;

            @Override
            protected Long doInBackground(Void... params) {
                try {
                    SQLiteDatabase db = getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(ConsultantEntry.COLUMN_NAME_ID, office.Id);
                    values.put(ConsultantEntry.COLUMN_NAME_NAME, office.Name);

                    return db.insert(ConsultantEntry.TABLE_NAME, null, values);
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

    public void getAllOffices(final OfficeDataArrayListener listener) {
        AsyncTask<Void, Integer, OfficeData[]> task = new AsyncTask<Void, Integer, OfficeData[]>() {
            private String errorMessage;

            @Override
            protected OfficeData[] doInBackground(Void... params) {
                try {
                    String[] projection = {
                            ConsultantEntry.COLUMN_NAME_ID,
                            ConsultantEntry.COLUMN_NAME_NAME,
                    };

                    List<OfficeData> result = new ArrayList<>();

                    SQLiteDatabase db = getReadableDatabase();
                    Cursor c = db.query(ConsultantEntry.TABLE_NAME, projection, null, null, null, null, null);
                    while (c.moveToNext()){
                        OfficeData oData = new OfficeData();
                        oData.Id = c.getInt(c.getColumnIndex(ConsultantEntry.COLUMN_NAME_ID));
                        oData.Name = c.getString(c.getColumnIndex(ConsultantEntry.COLUMN_NAME_NAME));

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
}
