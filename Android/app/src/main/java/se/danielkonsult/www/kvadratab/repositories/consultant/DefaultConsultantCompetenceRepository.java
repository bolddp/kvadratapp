package se.danielkonsult.www.kvadratab.repositories.consultant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;
import se.danielkonsult.www.kvadratab.services.prefs.DefaultPrefsService;

/**
 * Created by Daniel on 2016-10-06.
 */
public class DefaultConsultantCompetenceRepository implements ConsultantCompetenceRepository {

    // Private variables

    private final String[] queryProjection = {
            DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_CONSULTANT_ID,
            DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_INDEX,
            DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_COMPETENCE
    };
    private final String orderBy = DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_INDEX;

    private KvadratDb _db;

    // Private methods

    /**
     * Deletes the competences of a specific consultant.
     */
    private void delete(SQLiteDatabase db, int consultantId) {
        String whereClause = DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_CONSULTANT_ID + "=?";
        String[] whereArgs = new String[] { Integer.toString(consultantId) };
        db.delete(DbSpec.ConsultantCompetenceEntry.TABLE_NAME, whereClause, whereArgs);
    }

    // Constructor

    public DefaultConsultantCompetenceRepository(KvadratDb db) {
        _db = db;
    }

    @Override
    public String[] getById(int consultantId) {
        String selection = DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_CONSULTANT_ID + " = ?";
        String[] selectionArgs = {
                Integer.toString(consultantId)
        };

        List<String> result = new ArrayList<>();
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor c = db.query(DbSpec.ConsultantCompetenceEntry.TABLE_NAME, queryProjection, selection, selectionArgs, null, null, orderBy);
        while (c.moveToNext()) {
            result.add(c.getString(c.getColumnIndex(DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_COMPETENCE)));
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    public void update(int consultantId, String[] competences) {
        SQLiteDatabase db = _db.getWritableDatabase();

        delete(db, consultantId);

        // Add new ones
        int index = 0;
        for (String competence : competences){
            ContentValues values = new ContentValues();
            values.put(DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_CONSULTANT_ID, consultantId);
            values.put(DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_INDEX, index);
            values.put(DbSpec.ConsultantCompetenceEntry.COLUMN_NAME_COMPETENCE, competence);

            db.insertOrThrow(DbSpec.ConsultantCompetenceEntry.TABLE_NAME, null, values);
            index++;
        }
    }

    @Override
    public void delete(int consultantId) {
        SQLiteDatabase db = _db.getWritableDatabase();
        delete(db, consultantId);
    }
}
