package mohammedalhaq.github.io.locationbookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);

        adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,new String[] {"title","location"}, new int[] {android.R.id.text1, android.R.id.text2});
        //sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);

        try {


            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("title");
            String loc = bundle.getString("location");
            String notes = bundle.getString("notes");

            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("location", loc);
            data.add(map);

            /*
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("titleKey", title);
            editor.putString("locKey", loc);
            editor.putString("notesKey", notes);
            editor.commit();
            */
        } catch (Exception e){

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

    public void addLocation(View view){
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("source", "fab");
            startActivity(intent);
    }
}
