package com.example.assignment_4_1;

import static android.text.InputType.TYPE_CLASS_NUMBER;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LayoutParams viewLayoutParams = null;
    private EditText userFavoriteNumberEditText;
    private Random randomNumberGenerator = new Random();
    private StringBuilder stringBuilder;
    private int userFavoriteNumber, randomNumber;
    private String toastMessage;
    private final int RANDOM_UPPER_BOUND = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        viewLayoutParams.leftMargin = 40;
        viewLayoutParams.rightMargin = 40;
        viewLayoutParams.topMargin = 10;
        viewLayoutParams.bottomMargin = 10;

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        TextView randomNumberLabelTextView = new TextView(this);
        randomNumberLabelTextView.setText(R.string.random_number_label);
        randomNumberLabelTextView.setTextSize(19);
        randomNumberLabelTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        randomNumberLabelTextView.setLayoutParams(viewLayoutParams);
        linearLayout.addView(randomNumberLabelTextView);

        userFavoriteNumberEditText = new EditText(this);
        userFavoriteNumberEditText.setInputType(TYPE_CLASS_NUMBER);
        userFavoriteNumberEditText.setLayoutParams(viewLayoutParams);
        userFavoriteNumberEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
        linearLayout.addView(userFavoriteNumberEditText);

        userFavoriteNumberEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        if(!userFavoriteNumberEditText.getText().toString().isEmpty()){
                            compareNumbers();
                            return true;
                        }
                        Toast.makeText(MainActivity.this, R.string.field_needs_value, Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
        });

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.addContentView(linearLayout, linearLayoutParams);
    }

    private void compareNumbers(){
        stringBuilder = new StringBuilder();
        userFavoriteNumber = Integer.parseInt(userFavoriteNumberEditText.getText().toString());
        randomNumber = randomNumberGenerator.nextInt(RANDOM_UPPER_BOUND);

        if(userFavoriteNumber == randomNumber) {
            stringBuilder.append(getString(R.string.random_number_mach)).append("\n");
        } else{
            stringBuilder.append(getString(R.string.random_number_no_mach)).append("\n");
        }
        stringBuilder.append(getString(R.string.random_number, randomNumber));

        Toast.makeText(this, stringBuilder, Toast.LENGTH_LONG).show();

    };
}
