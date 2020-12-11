package com.mahanthesh.callrecoder786.fragments;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahanthesh.callrecoder786.Database.DatabaseClient;
import com.mahanthesh.callrecoder786.R;
import com.mahanthesh.callrecoder786.adapter.TasksAdapter;
import com.mahanthesh.callrecoder786.model.Task;
import com.santalu.emptyview.EmptyView;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.util.List;

public class AddFragment extends Fragment {
    private EmptyView emptyView;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

//        emptyView = (EmptyView) view.findViewById(R.id.empty_view);
//        emptyView.empty().show();
        
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        getTask();



        return view;
    }

    private void getTask() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }
            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(getContext(), tasks);
                recyclerView.setAdapter(adapter);
            }
        }
        GetTasks gt = new GetTasks();
        gt.execute();
    }
}
