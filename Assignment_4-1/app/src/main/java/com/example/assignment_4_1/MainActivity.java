package com.example.assignment_4_1;

import static android.text.InputType.TYPE_CLASS_NUMBER;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LayoutParams viewLayoutParams = null;
    private EditText randomNumberEditText;
    private Random randomNumberGenerator = new Random();
    private int enteredRandomNumber, randomNumber;
    private String toastMessage;
    private int MAX_RANDOM_NUMBER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Here we define parameters for views
        viewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewLayoutParams.leftMargin = 40;
        viewLayoutParams.rightMargin = 40;
        viewLayoutParams.topMargin = 10;
        viewLayoutParams.bottomMargin = 10;
        // Here we create the layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // Here we define a text view
        TextView randomNumberLabelTextView = new TextView(this);
        randomNumberLabelTextView.setText(R.string.random_number_label);
        randomNumberLabelTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(randomNumberLabelTextView);
        // Here we define the edit text
        randomNumberEditText = new EditText(this);
        randomNumberEditText.setInputType(TYPE_CLASS_NUMBER);
        randomNumberEditText.setLayoutParams(viewLayoutParams);
        linearLayout.addView(randomNumberEditText);

        //Here we define key listener
        randomNumberEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(keyCode==KeyEvent.KEYCODE_ENTER) {
                        if(!randomNumberEditText.getText().toString().isEmpty()){
                            checkRandomNumber();
                            return true;
                        }
                        randomNumberEditText.setBackgroundColor(Color.RED);
                    }

                    return false;
                }
        });

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        this.addContentView(linearLayout, linearLayoutParams);
    }

    private void checkRandomNumber(){
        enteredRandomNumber = Integer.parseInt(randomNumberEditText.getText().toString());
        randomNumber = randomNumberGenerator.nextInt(MAX_RANDOM_NUMBER);

        if(enteredRandomNumber == randomNumber) {
            toastMessage = getString(R.string.random_number_mach);
        } else{
            toastMessage = getString(R.string.random_number_no_mach);
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

    };
}