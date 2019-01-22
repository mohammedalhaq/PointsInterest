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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton add;
    List<String> titleList, locationList;
    List<Integer> imageList;
    ListViewAdapter adapter;


    static SQLiteDatabase db;
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

        add = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
        registerForContextMenu(listView);


        titleList = new ArrayList<>();
        locationList = new ArrayList<>();
        imageList = new ArrayList<>();

        adapter = new ListViewAdapter(MainActivity.this, titleList, locationList, imageList );

        refreshDB();
        try {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("title");
            String loc = bundle.getString("location");
            String notes = bundle.getString("notes");


            //dont insert nothing
            if ((title!=null) || (loc!=null)) {
                insertDB(title, loc, notes);
                Snackbar.make(findViewById(R.id.constraintLayout), title + " added", Snackbar.LENGTH_SHORT).show();
            }

            refreshDB();
        } catch (Exception e) {
            e.printStackTrace();
        }



        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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

        titleList.clear();
        locationList.clear();
        imageList.clear();

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

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContractDB.TaskEntry.COLUMN_NAME_TITLE));
            String loc = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContractDB.TaskEntry.COLUMN_NAME_LOC));

                titleList.add(title);
                locationList.add(loc);
                imageList.add(R.drawable.tempbbb);
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
        searchView.setOnCloseListener(queryExitListener);
        return true;
    }

    private SearchView.OnCloseListener queryExitListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            listView.setAdapter(adapter);

            return false;
        }
    };

    //handle seach queries TODO
    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener(){
        List<String> locationResults = new ArrayList<>();
        List<String> titleResults = new ArrayList<>();
        List<Integer> imgResults = new ArrayList<>();

        @Override
        public boolean onQueryTextSubmit(String query) {
            ListViewAdapter results = new ListViewAdapter(MainActivity.this, titleResults, locationResults, imgResults);

            titleResults.clear();
            locationResults.clear();
            imgResults.clear();

            for (int i = 0; i < locationList.size(); i++) {
                String location = locationList.get(i);
                String title = titleList.get(i);
                //int img = imageList.get(i);

                if ((location.toLowerCase().equals(query.toLowerCase())) || (title.toLowerCase().equals(query.toLowerCase()))) {
                    try {
                        locationResults.add(location);
                        titleResults.add(title);
                        imgResults.add(R.drawable.tempbbb);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            listView.setAdapter(results);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            int length = query.length();

            titleResults.clear();
            locationResults.clear();
            imgResults.clear();

            ListViewAdapter results = new ListViewAdapter(MainActivity.this, titleResults, locationResults, imgResults);

            for (int i = 0; i < locationList.size(); i++) {
                String location = locationList.get(i);
                String title = titleList.get(i);
                //int img = imageList.get(i);

                String tempLoc = location.substring(0,length);
                String tempTitle = title.substring(0, length);
                if ((tempLoc.toLowerCase().equals(query.toLowerCase())) || (tempTitle.toLowerCase().equals(query.toLowerCase()))) {
                    try {
                        locationResults.add(location);
                        titleResults.add(title);
                        imgResults.add(R.drawable.tempbbb);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            listView.setAdapter(results);
            return true;
        }

        };


}
