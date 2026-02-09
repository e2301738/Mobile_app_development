package com.example.assignment_2;

import android.content.res.Configuration;
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
    private TextView TextViewRandomNumber;
    int randomInt = 0;
    int intBeforeRotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextViewRandomNumber = findViewById(R.id.tvRandomNumber);
        TextView textViewRandomNumberBeforeRotation = findViewById(R.id.tvRandomNumberBeforeRotation);

        if (savedInstanceState != null) {
            intBeforeRotation = savedInstanceState.getInt("randomNumber");
            String savedTime = savedInstanceState.getString("dateTime");

            String randomNumberMessage = getString(R.string.randomNumberOrientationChange) + intBeforeRotation;
            String toastMessage = getString(R.string.currentDateAndTime) + savedTime;

            textViewRandomNumberBeforeRotation.setText(randomNumberMessage);
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }

        handler.post(randomNumberGenerator);
    }

    Runnable randomNumberGenerator = new Runnable() {
        @Override
        public void run() {
            intBeforeRotation = randomInt;
            randomInt = random.nextInt(10000);
            TextViewRandomNumber.setText(getString(R.string.currentRandomNumber) + randomInt);

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Date date = new Date();
        outState.putInt("randomNumber", randomInt);
        outState.putString("dateTime", String.valueOf(date));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(randomNumberGenerator);
    }
}