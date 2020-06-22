package com.example.todoapppetarsabev;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleList = findViewById(R.id.simpleList);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Add event listener to open Add task activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddToDo.class);
                startActivityForResult(intent, 200);
            }
        });

        simpleList = findViewById(R.id.simpleList);

        try {
            initDB();
            fetchTasks();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createUpdateActivity(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            try {
                Intent k = new Intent(MainActivity.this, Settings.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_pro_version) {
            try {
                Intent k = new Intent(MainActivity.this, ProVersion.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void initDB() throws SQLException {
        SQLiteDatabase db = null;

        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/tasks.db",
                null
        );

        String q = "CREATE TABLE if not exists `TASKS` ("
                + "`ID` integer primary key AUTOINCREMENT,"
                + "`name` text not null, "
                + "`date` text not null, "
                + "`hourFrom` text not null, "
                + "`hourTo` text not null, "
                + "`priority` integer, "
                + " unique(`name`) )";

        db.execSQL(q);
        db.close();
    }

    public void fetchTasks() throws SQLException {
        SQLiteDatabase db = null;

        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/tasks.db",
                null
        );

        simpleList.clearChoices();

        Cursor c = db.rawQuery("SELECT * FROM TASKS", null);
        ArrayList<String> listResults = new ArrayList<String>();

        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            Integer ID = c.getInt(c.getColumnIndex("ID"));

            listResults.add(ID + "\t" + name + "\t");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView,
                listResults
        );

        simpleList.setAdapter(arrayAdapter);
        db.close();
    }

    protected void createUpdateActivity(View view) {
        String selected = "";
        TextView clickedText = view.findViewById(R.id.textView);
        selected = clickedText.getText().toString();

        try {
            String[] elements = selected.split("\t");

            String ID = elements[0];
            String Name = elements[1];

            Intent intent = new Intent(MainActivity.this, Update.class);

            Bundle b = new Bundle();
            b.putString("ID", ID);
            b.putString("Name", Name);

            intent.putExtras(b);
            startActivityForResult(intent, 200, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
