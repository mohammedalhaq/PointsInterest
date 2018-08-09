package mohammedalhaq.github.io.locationbookmark;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.Task;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton add;
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    SimpleAdapter adapter;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);

        adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"title", "location"}, new int[]{android.R.id.text1, android.R.id.text2});

        //to read the db
        try {
            InputDB mDbHelper = new InputDB(this);
            db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    ContractDB.TaskEntry.COLUMN_NAME_TITLE,
                    ContractDB.TaskEntry.COLUMN_NAME_LOC,
                    ContractDB.TaskEntry.COLUMN_NAME_NOTES
            };


            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("title");
            String loc = bundle.getString("location");
            String notes = bundle.getString("notes");

            insertDB(title, loc, notes);

            //to parse the db
            Cursor cursor = db.query(
                    ContractDB.TaskEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();

                title = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContractDB.TaskEntry.COLUMN_NAME_TITLE));
                loc = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContractDB.TaskEntry.COLUMN_NAME_LOC));

                map.put("title", title);
                map.put("location", loc);
                data.add(map);

            }
            cursor.close();
        } catch (Exception e) {

            e.printStackTrace();

        }


        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("source", "details");
                startActivity(intent);
            }
        });
    }

    //fab button handler
    public void addLocation(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("source", "fab");
        startActivity(intent);
    }

    //inserts into the db
    public void insertDB(String title, String loc, String notes) {
        ContentValues values = new ContentValues();
        values.put(ContractDB.TaskEntry.COLUMN_NAME_TITLE, title);
        values.put(ContractDB.TaskEntry.COLUMN_NAME_LOC, loc);
        values.put(ContractDB.TaskEntry.COLUMN_NAME_NOTES, notes);

        //long newRowId = db.insert(ContractDB.TaskEntry.TABLE_NAME, null, values);

    }


    //creates search bar on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        /*
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
*/
        return true;
    }

    //search button handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {

        }
        return super.onOptionsItemSelected(item);

    }
}
