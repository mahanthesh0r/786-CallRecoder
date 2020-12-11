package com.mahanthesh.callrecoder786.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mahanthesh.callrecoder786.AddTaskActivity;
import com.mahanthesh.callrecoder786.Database.DatabaseClient;
import com.mahanthesh.callrecoder786.R;
import com.mahanthesh.callrecoder786.model.Report;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.List;


public class HomeFragment extends Fragment {

    private TextView txtViewTotalRecords, txtViewTotalTasks;
    private Storage storage;
    private Button btnAddReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtViewTotalRecords = (TextView) view.findViewById(R.id.textViewTotalRecords);
        txtViewTotalTasks = (TextView) view.findViewById(R.id.textViewTotalTasks);
        btnAddReminder = (Button) view.findViewById(R.id.buttonAddReminder);
        storage = new Storage(getContext());
        Report report = new Report();

        //get external storage
        String path = storage.getExternalStorageDirectory() + "/RecordDirName";
        final String dirPath = Environment.getExternalStorageDirectory() + "/RecordDirName";
        List<File> files = storage.getFiles(dirPath);
        report.setTotalRecords(files.size());
        Log.e("TAG","COUNT: "+ report.getTotalRecords());
        txtViewTotalRecords.setText(String.valueOf(report.getTotalRecords()));

        getCount();


        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddTaskActivity.class));
            }
        });


        return view;

    }




    private void getCount() {
        class GetCount extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                int totalCount = DatabaseClient
                        .getInstance(getContext())
                        .getAppDatabase()
                        .taskDao()
                        .getCount();
                return totalCount;
            }
            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);
                txtViewTotalTasks.setText(String.valueOf(count));

            }
        }
        GetCount gc = new GetCount();
        gc.execute();
    }
}
