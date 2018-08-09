package mohammedalhaq.github.io.locationbookmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InputDB extends SQLiteOpenHelper {

    public InputDB(Context context){
        super(context, ContractDB.DB_NAME,null,ContractDB.DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + ContractDB.TaskEntry.TABLE_NAME + " (" +
                ContractDB.TaskEntry.COLUMN_NAME_TITLE + " TEXT, " + ContractDB.TaskEntry.COLUMN_NAME_LOC + " TEXT, "
                + ContractDB.TaskEntry.COLUMN_NAME_NOTES + " TEXT );";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractDB.TaskEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
