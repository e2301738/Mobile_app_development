package com.example.assignment_2;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Random random = new Random();
    private TextView tvCurrentNumber;
    private int currentRandomValue = 0;
    private static final String KEY_RANDOM_NUMBER = "current_random_number";
    private static final String KEY_DATE_TIME = "saved_date_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvCurrentNumber = findViewById(R.id.tvRandomNumber);
        TextView tvOldNumber = findViewById(R.id.tvRandomNumberBeforeRotation);

        if (savedInstanceState != null) {
            int lastValueBeforeRotate = savedInstanceState.getInt(KEY_RANDOM_NUMBER);
            String savedTime = savedInstanceState.getString(KEY_DATE_TIME);

            String randomNumberMessage = getString(R.string.randomNumberOrientationChange) + " " + lastValueBeforeRotate;
            String toastMessage = getString(R.string.currentDateAndTime) + " " + savedTime;

            tvOldNumber.setText(randomNumberMessage);
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }

        handler.post(numberUpdateTask);
    }

    Runnable numberUpdateTask = new Runnable() {
        @Override
        public void run() {
            currentRandomValue = random.nextInt(10000);
            tvCurrentNumber.setText(getString(R.string.currentRandomNumber) + currentRandomValue);

            handler.postDelayed(this, 1000);
        };
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Date date = new Date();
        outState.putInt(KEY_RANDOM_NUMBER, currentRandomValue);
        outState.putString(KEY_DATE_TIME, String.valueOf(date));
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(numberUpdateTask);
    };
}