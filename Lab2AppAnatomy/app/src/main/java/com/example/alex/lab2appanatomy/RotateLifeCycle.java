package com.example.alex.lab2appanatomy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RotateLifeCycle extends AppCompatActivity {

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifeCycle","onCreate() called");
        setContentView(R.layout.activity_rotate_life_cycle);

        // recovering the instance state
        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("COUNTER_KEY");
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(counter));

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(myListener);
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener myListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            TextView textView = (TextView) findViewById(R.id.textView);
            counter += 1;
            textView.setText(Integer.toString(counter));
        }
    };

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("COUNTER_KEY", counter);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
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
