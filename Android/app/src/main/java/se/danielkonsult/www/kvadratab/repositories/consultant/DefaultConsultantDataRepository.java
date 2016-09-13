package se.danielkonsult.www.kvadratab.repositories.consultant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.helpers.Utils;
import se.danielkonsult.www.kvadratab.helpers.db.DbDataListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbOperationListener;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

/**
 * Handles database reading and writing of consultant data.
 */
public class DefaultConsultantDataRepository implements ConsultantDataRepository {

    // Private variables

    private KvadratDb _db;

    // Constructor

    public DefaultConsultantDataRepository(KvadratDb _db) {
        this._db = _db;
    }

    @Override
    public void getAll(final DbDataListener<ConsultantData[]> listener) {
        AsyncTask<Void, Integer, ConsultantData[]> task = new AsyncTask<Void, Integer, ConsultantData[]>() {
            private String errorMessage;

            @Override
            protected ConsultantData[] doInBackground(Void... params) {
                try {
                    String[] projection = {
                            DbSpec.ConsultantEntry.COLUMN_NAME_ID,
                            DbSpec.ConsultantEntry.COLUMN_NAME_NAME,
                            DbSpec.ConsultantEntry.COLUMN_NAME_JOBROLE,
                            DbSpec.ConsultantEntry.COLUMN_NAME_DESCRIPTION,
                            DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID
                    };

                    List<ConsultantData> result = new ArrayList<>();

                    SQLiteDatabase db = _db.getReadableDatabase();
                    Cursor c = db.query(DbSpec.ConsultantEntry.TABLE_NAME, projection, null, null, null, null, null);
                    while (c.moveToNext()){
                        ConsultantData consultantData = new ConsultantData();
                        consultantData.Id = c.getInt(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_ID));
                        consultantData.Name = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_NAME));
                        consultantData.JobRole = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_JOBROLE));
                        consultantData.Description = c.getString(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_DESCRIPTION));
                        consultantData.OfficeId = c.getInt(c.getColumnIndex(DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID));

                        result.add(consultantData);
                    }

                    return result.toArray(new ConsultantData[result.size()]);
                }
                catch (Throwable ex){
                    errorMessage = ex.getMessage();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ConsultantData[] consultantDatas) {
                if (consultantDatas == null)
                    listener.onError(errorMessage);
                else
                    listener.onResult(consultantDatas);
            }
        };
        task.execute();

    }

    @Override
    public void insert(final ConsultantData consultant, final DbOperationListener listener) {
        // Setup the task to perform the insertion
        AsyncTask<Void, Integer, Long> task = new AsyncTask<Void, Integer, Long>() {

            private String errorMessage;

            @Override
            protected Long doInBackground(Void... params) {
                try {
                    SQLiteDatabase db = _db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DbSpec.ConsultantEntry.COLUMN_NAME_ID, consultant.Id);
                    values.put(DbSpec.ConsultantEntry.COLUMN_NAME_NAME, consultant.Name);
                    if (!Utils.isStringNullOrEmpty(consultant.JobRole))
                        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_JOBROLE, consultant.JobRole);
                    if (!Utils.isStringNullOrEmpty(consultant.Description))
                        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_DESCRIPTION, consultant.Description);

                    // Insert office data
                    if (consultant.OfficeId != 0)
                        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID, consultant.OfficeId);
                    else if ((consultant.Office != null) && (consultant.Office.Id != 0))
                        values.put(DbSpec.ConsultantEntry.COLUMN_NAME_OFFICEID, consultant.Office.Id);

                    return db.insert(DbSpec.ConsultantEntry.TABLE_NAME, null, values);
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
