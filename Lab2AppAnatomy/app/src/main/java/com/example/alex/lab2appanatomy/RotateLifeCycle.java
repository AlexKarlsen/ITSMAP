package com.example.alex.lab2appanatomy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class RotateLifeCycle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifeCycle","onCreate() called");
        setContentView(R.layout.activity_rotate_life_cycle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LifeCycle","onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LifeCycle", "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LifeCycle", "onPause() Called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("LifeCycle", "onStop() Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle", "onDestroy() called");
    }
}
