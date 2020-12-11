package com.mahanthesh.callrecoder786.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahanthesh.callrecoder786.R;
import com.mahanthesh.callrecoder786.UpdateTaskActivity;
import com.mahanthesh.callrecoder786.model.Task;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder>{
    private Context mCtx;
    private List<Task> taskList;
    MediaPlayer mp = new MediaPlayer();
    MaterialDialog materialDialog;
    private String recordFilePath;

    public TasksAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_tasks, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.textViewTitle.setText(t.getTask());
        holder.textViewDesc.setText(t.getDesc());
        holder.textViewReminder.setText(t.getReminder());
        recordFilePath = t.getAudioPath();


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView  textViewTitle, textViewDesc, textViewReminder;
        Button playBtn;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewReminder = itemView.findViewById(R.id.textViewReminder);
            playBtn = itemView.findViewById(R.id.playBtn);

            if(recordFilePath == null){
                playBtn.setVisibility(View.INVISIBLE);
            }



            playBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == playBtn.getId()) {
                try {
                    mp.reset();
                    mp.setDataSource(recordFilePath);
                    mp.prepare();
                    mp.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Task task = taskList.get(getAdapterPosition());

                Intent intent = new Intent(mCtx, UpdateTaskActivity.class);
                intent.putExtra("task", task);

                mCtx.startActivity(intent);

            }

        }
    }


    }
