package com.mahanthesh.callrecoder786;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanthesh.callrecoder786.Database.DatabaseClient;
import com.mahanthesh.callrecoder786.model.Task;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDesc;
    private Button btnAddReminder, btnDeleteTask, btnUpdate;
    private TextView textViewReminderTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        //init
        init();
    }

    private void init() {

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);
        btnAddReminder = (Button) findViewById(R.id.buttonReminder);
        btnDeleteTask = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.buttonUpdate);
        textViewReminderTime = (TextView) findViewById(R.id.textViewReminderTime);

        final Task task = (Task) getIntent().getSerializableExtra("task");

        loadTask(task);

        //Listeners
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask(task);
            }
        });

        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(task);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();

            }
        });

    }

    private void loadTask(Task task) {
        editTextTitle.setText(task.getTask());
        editTextDesc.setText(task.getDesc());
        textViewReminderTime.setText(task.getReminder());

    }

    private void updateTask(final Task task){
        final String sTitle = editTextTitle.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sReminderText = textViewReminderTime.getText().toString().trim();

        if (sTitle.isEmpty()) {
            editTextTitle.setError("Task required");
            editTextTitle.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setTask(sTitle);
                task.setDesc(sDesc);
                task.setReminder(sReminderText);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}