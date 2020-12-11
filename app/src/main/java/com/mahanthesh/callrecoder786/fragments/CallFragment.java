package com.mahanthesh.callrecoder786.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mahanthesh.callrecoder786.AddTaskActivity;
import com.mahanthesh.callrecoder786.R;
import com.mahanthesh.callrecoder786.model.Report;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CallFragment extends Fragment {

    private Storage storage;
    private ListView listView;
    MediaPlayer mp = new MediaPlayer();
    MaterialDialog materialDialog;
    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        //init
        storage = new Storage(getContext());
        listView = (ListView) view.findViewById(R.id.call_listview);

        //get external storage
        String path = storage.getExternalStorageDirectory() + "/RecordDirName";
        final String dirPath = Environment.getExternalStorageDirectory() + "/RecordDirName";

        Log.d("TAG", "PATH1: "+ dirPath);

        List<File> files = storage.getFiles(path);
        Report report = new Report();
        report.setTotalRecords(files.size());
        final ArrayList<String> arrayList = new ArrayList<>();

        for(int i = 0; i<files.size(); i++){
            String fileNameWithPath = files.get(i).toString();
            String[] filename = fileNameWithPath.split("/");
            arrayList.add(filename[5]);

        }



        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "file: "+ arrayList.get(i).toString(), Toast.LENGTH_LONG).show();
                try {
                    mp.reset();
                    mp.setDataSource(dirPath+ "/" + arrayList.get(i));
                    mp.prepare();
                    mp.start();
                    id = i;

                    materialDialog = new MaterialDialog.Builder(getActivity())
                            .setTitle("Call Recoder Media Player")
                            .setCancelable(true)
                            .setPositiveButton("Add Note", R.drawable.ic_add_note, new MaterialDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                Log.d("TAG", "PATH: "+ dirPath+ "/" + arrayList.get(id));
                                String recordFilePath = dirPath+ "/" + arrayList.get(id);
                                Intent addTaskIntent = new Intent(getActivity(), AddTaskActivity.class);
                                addTaskIntent.putExtra("recordFilePath",recordFilePath);
                                startActivity(addTaskIntent);
                                dialogInterface.dismiss();



                                }
                            })
                            .setNegativeButton("Stop", R.drawable.ic_stop, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    mp.stop();
                                    mp.reset();
                                    dialogInterface.dismiss();

                                }
                            }).build();

                    // Show Dialog
                    materialDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });




        return view;
    }
}
