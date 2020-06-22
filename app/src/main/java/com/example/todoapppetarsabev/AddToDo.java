package com.example.todoapppetarsabev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddToDo extends AppCompatActivity {
    protected Integer priority;
    protected RadioGroup radioGroup;
    protected EditText name, date, hourFrom, hourTo;

    protected Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        name = findViewById(R.id.nameAdd);
        date = findViewById(R.id.dateAdd);
        hourFrom = findViewById(R.id.hourFromAdd);
        hourTo = findViewById(R.id.hourToAdd);
        radioGroup = findViewById(R.id.radio_group_add);

        priority = 1;
        ((RadioButton)radioGroup.getChildAt(priority - 1)).setChecked(true);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
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


    protected void saveTask() {
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

            String q = "INSERT INTO TASKS (name, date, hourFrom, hourTo, priority) VALUES(?, ?, ?, ?, ?)";

            db.execSQL(q, new Object[]{nameData, dateData, hourFromData, hourToData, priorityData});

            Toast.makeText(getApplicationContext(), "Insert successfull", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.v("INSERT DATA INTO TABLE", "Exception=" + e.getLocalizedMessage());
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
        Intent i = new Intent(AddToDo.this, MainActivity.class);
        startActivity(i);
    }
}
