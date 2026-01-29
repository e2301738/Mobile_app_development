package com.example.assignment_1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private long lastTimestamp;
    private String tag="EVH_Demo: ";

    private void logTime(String eventName){
        long currentTime = System.currentTimeMillis();
        long eventTime = currentTime - lastTimestamp;
        Log.d(tag, eventName + " event time: " + eventTime + "ms");
        lastTimestamp = currentTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lastTimestamp = System.currentTimeMillis();
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