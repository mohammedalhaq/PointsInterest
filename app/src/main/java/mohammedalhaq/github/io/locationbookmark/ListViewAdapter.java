package mohammedalhaq.github.io.locationbookmark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<String> nameArray, infoArray;
    private final List<Integer> imageIDarray;

    SQLiteDatabase db;

    public ListViewAdapter(Activity context, List<String> nameArrayParam, List<String> infoArrayParam, List<Integer> imageIDArrayParam){
        super(context,R.layout.listview_row , nameArrayParam);
        db = MainActivity.getDb();

        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        TextView nameTextField = rowView.findViewById(R.id.title);
        TextView infoTextField = rowView.findViewById(R.id.location);
        ImageView imageView = rowView.findViewById(R.id.imageView);

        final String nameOf = nameArray.get(position);
        final String locOf = infoArray.get(position);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameOf);
        infoTextField.setText(locOf);
        //imageView.setImageResource(imageIDarray.get(position));
        imageView.setImageResource(R.drawable.tempbbb);

        //https://stackoverflow.com/questions/27235891/onclick-listener-for-custom-listview
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsView.class);
                intent.putExtra("name", nameOf);
                intent.putExtra("location", locOf);
                context.startActivity(intent);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete "+ nameOf + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String query = "DELETE FROM " + ContractDB.TaskEntry.TABLE_NAME + " WHERE " +
                                        ContractDB.TaskEntry.COLUMN_NAME_LOC + " = " + locOf;
                                db.execSQL(query);
                                //db.delete(ContractDB.TaskEntry.TABLE_NAME, ContractDB.TaskEntry.COLUMN_NAME_LOC, locOf);
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.create();

                return true;
            }
        });

        rowView.setClickable(true);
        return rowView;
    };
}
