package com.mahanthesh.callrecoder786;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;

import com.aykuttasil.callrecord.CallRecord;
import com.mahanthesh.callrecoder786.services.CallDetectService;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {

    public static CallRecord callRecord;
    private SmoothBottomBar bottomBar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission();
        startService(new Intent(MainActivity.this, CallDetectService.class));
        //init widgets
        init();
        //bottombar Listner
        bottombarListener();

    }

    private void init(){
        bottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
        navController = Navigation.findNavController(this, R.id.fragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        bottomBar.setupWithNavController(menu, navController);
        return true;
    }

    private void bottombarListener(){
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
               return false;

            }
        });
    }


    /**
     * Ask for the required permissions
     */
    private void askPermission() {
        CheckPermission checkPermission = new CheckPermission();
        checkPermission.checkPermission(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}