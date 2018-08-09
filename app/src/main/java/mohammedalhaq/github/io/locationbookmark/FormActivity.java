package mohammedalhaq.github.io.locationbookmark;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {
    EditText nameText, locationText, notes;
    boolean toggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle extras = getIntent().getExtras();
        final String loc = extras.getString("location");
        locationText = findViewById(R.id.loc);
        nameText = findViewById(R.id.name);
        notes = findViewById(R.id.notes);


        locationText.setText(loc, TextView.BufferType.EDITABLE);
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormActivity.this, MapsActivity.class);
                intent.putExtra("location", loc);
                intent.putExtra("source", "form");
                startActivity(intent);
            }
        });

        //toggles so that tapping opens maps or edittext
        locationText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (toggle) {
                    toggle = false;
                } else {
                    toggle = true;
                }
                locationText.setFocusable(toggle);
                return false;
            }
        });
    }


    //add changes
    public void confirm(View view){
        Intent intent = new Intent(FormActivity.this, MainActivity.class);

        intent.putExtra("title", nameText.getText().toString());
        intent.putExtra("location", locationText.getText().toString());
        intent.putExtra("notes", notes.getText().toString());

        Toast.makeText(this, locationText.getText().toString() + " added", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }

}
