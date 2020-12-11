package com.mahanthesh.callrecoder786;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mahanthesh.callrecoder786.Database.DatabaseClient;
import com.mahanthesh.callrecoder786.fragments.TimePickerFragment;
import com.mahanthesh.callrecoder786.model.Task;
import com.mahanthesh.callrecoder786.services.AlertReceiver;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private EditText et_title, et_desc;
    private Button reminderBtn, saveBtn;
    private String recordFilepath;
    private TextView txtViewReminderTime;
    private String RemindertimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        //init
        init();


        Intent intent = getIntent();
        if(intent != null){
            recordFilepath = intent.getStringExtra("recordFilePath");
        }


    }

    private void init() {
        et_title = (EditText) findViewById(R.id.editTextTitle);
        et_desc = (EditText) findViewById(R.id.editTextDesc);
        saveBtn = (Button) findViewById(R.id.buttonSave);
        reminderBtn = (Button) findViewById(R.id.buttonReminder);
        txtViewReminderTime = (TextView) findViewById(R.id.textViewReminderTime);
        //Listeners
        saveBtn.setOnClickListener(this);
        reminderBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.buttonSave:
                saveTask();
                break;
            case R.id.buttonReminder:
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                break;
        }
    }

    private void saveTask() {
        final String sTask = et_title.getText().toString().trim();
        final String sDesc = et_desc.getText().toString().trim();
        final String sRecordFilePath = recordFilepath;
        final String sReminderText = RemindertimeText;

        if (sTask.isEmpty()) {
            et_title.setError("Task required");
            et_title.requestFocus();
            return;
        }
        if (sDesc.isEmpty()) {
            et_desc.setError("Desc required");
            et_desc.requestFocus();
            return;
        }
//        if(sRecordFilePath.isEmpty()){
//            Toast.makeText(getApplicationContext(), "Audio file not found", Toast.LENGTH_LONG).show();
//        }

        class SaveTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                //creating a task
                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setAudioPath(sRecordFilePath);
                task.setReminder(sReminderText);
                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE,i1);
        c.set(Calendar.SECOND,0);

        updateTimeTextView(c);
        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void updateTimeTextView(Calendar c) {

        RemindertimeText = "Reminder Set for: ";
        RemindertimeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        txtViewReminderTime.setText(RemindertimeText);

    }
}