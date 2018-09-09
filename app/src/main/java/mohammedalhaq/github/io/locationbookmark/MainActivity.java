package mohammedalhaq.github.io.locationbookmark;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

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
    boolean initial;
    String[] projection = { //for initializing cursor
            ContractDB.TaskEntry.COLUMN_NAME_TITLE,
            ContractDB.TaskEntry.COLUMN_NAME_LOC,
            ContractDB.TaskEntry.COLUMN_NAME_NOTES
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");
        initial = true;
        add = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
        registerForContextMenu(listView);

        adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"title", "location"}, new int[]{android.R.id.text1, android.R.id.text2});

        //to read the db
            refreshDB();
            initial = false;
        try {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("title");
            String loc = bundle.getString("location");
            String notes = bundle.getString("notes");

            //dont insertt nothing
            if (loc != null) insertDB(title, loc, notes);
            //adapter.clear();
            refreshDB();
        } catch (Exception e) {
            e.printStackTrace();
        }



        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //open up a an activity to view the location and passes extra info in case of edit
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MapsView.class);
                HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
                intent.putExtra("name", map.get("title"));
                String location = map.get("location");
                intent.putExtra("location", location);
                //TODO get notes column
                String query = "SELECT " + ContractDB.TaskEntry.COLUMN_NAME_NOTES + " FROM " +
                        ContractDB.TaskEntry.TABLE_NAME + " WHERE " + ContractDB.TaskEntry.COLUMN_NAME_LOC
                        + "=" + location + ";";

                //ResultSet rs = db.execSQL(query);

                //intent.putExtra("desc", );
                startActivity(intent);
            }
        });

       //delete entry on long click
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Delete "+ adapterView.getItemAtPosition(i).toString() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //https://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
                                //adapterView.getAdapter().(i);
                            }
                        });
                return false;
            }
            });
    }

    //fab button handler
    public void addLocation(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("source", "fab");
        startActivity(intent);
    }

    //reads through db and updates
    public void refreshDB(){
        InputDB mDbHelper = new InputDB(this);
        db = mDbHelper.getReadableDatabase();

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

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContractDB.TaskEntry.COLUMN_NAME_TITLE));
            String loc = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContractDB.TaskEntry.COLUMN_NAME_LOC));

            //if (initial) {
                map.put("title", title);
                map.put("location", loc);
                data.add(map);
            //}
        }
        cursor.close();
    }

    //inserts into the db
    public void insertDB(String title, String loc, String notes) {
        ContentValues values = new ContentValues();
        values.put(ContractDB.TaskEntry.COLUMN_NAME_TITLE, title);
        values.put(ContractDB.TaskEntry.COLUMN_NAME_LOC, loc);
        values.put(ContractDB.TaskEntry.COLUMN_NAME_NOTES, notes);

        db.insertWithOnConflict(ContractDB.TaskEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);

    }


    //creates search bar on action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    //handle seach queries
    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
    {
        @Override
        public boolean onQueryTextSubmit(String query) {
            for (int i = 0; i < data.size(); i++) {
                String m = data.get(i).keySet().toString();
                if (m.equals(query)) {
                    //add to list
                    System.out.println("matches");

                }
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            int length = query.length();
            for(int i =0;i<data.size();i++){
                String m = data.get(i).keySet().toString().substring(0,length);
                if (m.equals(query)){
                    //add to list
                    System.out.println("matches");
                }
            }


            return true;
        }
    };


    //search button handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {

        }
        return super.onOptionsItemSelected(item);

    }
}
