package com.example.todoapppetarsabev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Update extends AppCompatActivity {
    protected String ID;

    protected Integer priority;
    protected RadioGroup radioGroup;
    protected EditText name, date, hourFrom, hourTo;

    protected Button btnUpdate, btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        hourFrom = findViewById(R.id.hourFrom);
        hourTo = findViewById(R.id.hourTo);
        radioGroup = findViewById(R.id.radio_group);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDone = findViewById(R.id.btnDone);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            ID = b.getString("ID");
            getTaskById();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAsDone();
            }
        });
    }

    public void getTaskById() {
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openOrCreateDatabase(
                    getFilesDir().getPath() + "/tasks.db",
                    null
            );

            Cursor c = db.rawQuery( ("SELECT * FROM TASKS WHERE ID=" + ID), null);
            while(c.moveToNext()) {
                name.setText(c.getString(c.getColumnIndex("name")));
                date.setText(c.getString(c.getColumnIndex("date")));
                hourFrom.setText(c.getString(c.getColumnIndex("hourFrom")));
                hourTo.setText(c.getString(c.getColumnIndex("hourTo")));
                priority = c.getInt(c.getColumnIndex("priority"));
                ((RadioButton)radioGroup.getChildAt(priority - 1)).setChecked(true);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_low:
                if (checked)
                    priority = 1;
                    break;
            case R.id.radio_medium:
                if (checked)
                    priority = 2;
                    break;
                case R.id.radio_high:
                if (checked)
                    priority = 3;
                    break;
        }
    }

    protected void setAsDone() {
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openOrCreateDatabase(
                    getFilesDir().getPath() + "/tasks.db",
                    null
            );

            String q = "DELETE FROM TASKS WHERE ID=?";

            db.execSQL(q, new Object[]{ID});

            Toast.makeText(getApplicationContext(), "Delete is successfull", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(db != null) {
                db.close();
                db = null;
            }
        }
        backToMainActivity();
    }

    protected void updateTask() {
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openOrCreateDatabase(
                    getFilesDir().getPath() + "/tasks.db",
                    null
            );

            String nameData = name.getText().toString();
            String dateData = date.getText().toString();
            String hourFromData = hourFrom.getText().toString();
            String hourToData = hourTo.getText().toString();
            Integer priorityData = priority;

            String q = "UPDATE TASKS SET name=?, date=?, hourFrom=?, hourTo=?, priority=? WHERE ID=?";

            db.execSQL(q, new Object[]{nameData, dateData, hourFromData, hourToData, priorityData, ID});

            Toast.makeText(getApplicationContext(), "Update successfull", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(db != null) {
                db.close();
                db = null;
            }
        }
        backToMainActivity();
    }

    protected void backToMainActivity() {
        finishActivity(200);
        Intent i = new Intent(Update.this, MainActivity.class);
        startActivity(i);
    }
}
