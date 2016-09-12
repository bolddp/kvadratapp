package se.danielkonsult.www.kvadratab.helpers.db;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 2016-09-08.
 */
public class DbSpec {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class OfficeEntry implements BaseColumns {
        public static final String TABLE_NAME = "office";
        public static final String COLUMN_NAME_ID = "webid";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NAME + TEXT_TYPE + " )";
        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ConsultantEntry implements BaseColumns {
        public static final String TABLE_NAME = "consultant";
        public static final String COLUMN_NAME_ID = "webid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_OFFICEID = "office_id";
        public static final String COLUMN_NAME_JOBROLE = "jobrole";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_JOBROLE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DESCRIPTION + TEXT_TYPE + " )";
        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}