package mohammedalhaq.github.io.locationbookmark;

import android.provider.BaseColumns;

public class ContractDB {
    public static final String DB_NAME = "com.example.db";
    public static final int DB_VERSION = 2;


    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "LocationsDB";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LOC = "loc";
        public static final String COLUMN_NAME_NOTES = "notes";
    }
}
