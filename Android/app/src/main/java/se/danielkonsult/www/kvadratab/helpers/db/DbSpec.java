package se.danielkonsult.www.kvadratab.helpers.db;

import android.nfc.Tag;
import android.provider.BaseColumns;

/**
 * Created by Daniel on 2016-09-08.
 */
public class DbSpec {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEP = ",";

    public static class OfficeEntry {
        public static final String TABLE_NAME = "office";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                COLUMN_NAME_NAME + TEXT_TYPE + " )";
        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class TagEntry {
        public static final String TABLE_NAME = "tag";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                COLUMN_NAME_NAME + TEXT_TYPE + " )";
        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ConsultantEntry {
        public static final String TABLE_NAME = "consultant";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_OFFICEID = "office_id";
        public static final String COLUMN_NAME_JOBROLE = "jobrole";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LASTNAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_JOBROLE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OFFICEID + INTEGER_TYPE + COMMA_SEP +
                        "FOREIGN KEY(" + COLUMN_NAME_OFFICEID + ") REFERENCES " +
                        OfficeEntry.TABLE_NAME + "(" + OfficeEntry.COLUMN_NAME_ID + "))";

        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_COUNT_ALL = "SELECT COUNT(*) FROM " + TABLE_NAME;

        public static final String SQL_UPDATE_OFFICE_ID = "UPDATE "+ TABLE_NAME +" SET "+COLUMN_NAME_OFFICEID + "= %d WHERE id = %d";
    }

    public static class ConsultantTagEntry{
        public static final String TABLE_NAME = "consultant_tag";
        public static final String COLUMN_NAME_CONSULTANT_ID = "consultant_id";
        public static final String COLUMN_NAME_TAG_ID = "tag_id";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CONSULTANT_ID + " INTEGER,"+
                        COLUMN_NAME_TAG_ID + " INTEGER," +
                        "PRIMARY KEY(" + COLUMN_NAME_CONSULTANT_ID + ", " + COLUMN_NAME_TAG_ID + "),"+
                        "FOREIGN KEY(" + COLUMN_NAME_CONSULTANT_ID + ") REFERENCES " +
                        ConsultantEntry.TABLE_NAME + "(" + ConsultantEntry.COLUMN_NAME_ID + ")," +
                        "FOREIGN KEY(" + COLUMN_NAME_TAG_ID + ") REFERENCES " +
                        TagEntry.TABLE_NAME + "(" + TagEntry.COLUMN_NAME_ID + "))";

        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class NotificationEntry {
        public static final String TABLE_NAME = "notification";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DATA = "data";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DATA + TEXT_TYPE + ")";

        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;


    }
}
