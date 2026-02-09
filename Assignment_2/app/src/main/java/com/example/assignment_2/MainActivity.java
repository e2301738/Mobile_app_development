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
    private int lastValueBeforeRotate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvCurrentNumber = findViewById(R.id.tvRandomNumber);
        TextView tvOldNumber = findViewById(R.id.tvRandomNumberBeforeRotation);

        if (savedInstanceState != null) {
            lastValueBeforeRotate = savedInstanceState.getInt("randomNumber");
            String savedTime = savedInstanceState.getString("dateTime");

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
            lastValueBeforeRotate = currentRandomValue;
            currentRandomValue = random.nextInt(10000);
            tvCurrentNumber.setText(getString(R.string.currentRandomNumber) + currentRandomValue);

            handler.postDelayed(this, 1000);
        };
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Date date = new Date();
        outState.putInt("randomNumber", currentRandomValue);
        outState.putString("dateTime", String.valueOf(date));
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(numberUpdateTask);
    };
}