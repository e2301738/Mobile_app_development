package com.example.assignment_1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    long eventCreationTime;
    String tag="EVH_Demo: ";

    private void logTime(String event){
        long currentTime = System.currentTimeMillis();
        long eventTime = currentTime - eventCreationTime;
        Log.d(tag, event + "time difference: " + eventTime + "ms");
        eventCreationTime = currentTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventCreationTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logTime("onCreate()");
    }
    protected void onStart() {
        super.onStart();
        logTime("onStart()");
    }
    protected void onRestart() {
        super.onRestart();
        logTime("onReStart()");
    }
    protected void onResume() {
        super.onResume();
        logTime("onResume()");
    }
    protected void onPause() {
        super.onPause();
        logTime("onPause()");
    }
    protected void onStop() {
        super.onStop();
        logTime("onStop()");
    }
    protected void onDestroy() {
        super.onDestroy();
        logTime("onDestroy()");
    }
}