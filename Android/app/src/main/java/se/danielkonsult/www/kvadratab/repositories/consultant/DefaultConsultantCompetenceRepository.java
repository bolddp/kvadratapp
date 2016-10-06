package se.danielkonsult.www.kvadratab.repositories.consultant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.db.DbSpec;
import se.danielkonsult.www.kvadratab.helpers.db.KvadratDb;

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

        // Clear any old competence entries and then add the new ones
        String sql = String.format(DbSpec.ConsultantCompetenceEntry.SQL_CLEAR_BY_CONSULTANT_ID, consultantId);
        db.execSQL(sql);
        int index = 0;
        for (String competence : competences){
            sql = String.format(DbSpec.ConsultantCompetenceEntry.SQL_INSERT, consultantId, index, competence);
            db.execSQL(sql);
            index++;
        }
    }
}
