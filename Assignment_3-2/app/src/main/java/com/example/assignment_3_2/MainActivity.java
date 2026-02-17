package com.example.assignment_3_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView calculatorTextView;
    private double selectedNumber;
    private String currentOperator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculatorTextView = findViewById(R.id.calcDisplay);
    }

    public void onNumberClick(View v) {
        Button numberButton = (Button) v;
        selectedNumber = Double.parseDouble(numberButton.getText().toString());
        calculatorTextView.append(numberButton.getText().toString());
    }

    public void onOperatorClick(View v) {
        currentOperator = ((Button) v).getText().toString();
        calculatorTextView.setText("");
    }

    public void onEqualClick(View v) {
        if(!calculatorTextView.getText().toString().isEmpty()){
            double secondValue = Double.parseDouble(calculatorTextView.getText().toString());
            double result = 0;

            switch (currentOperator) {
                case "+":
                    result = selectedNumber + secondValue;
                    break;
                case "−":
                    result = selectedNumber - secondValue;
                    break;
                case "×":
                    result = selectedNumber * secondValue;
                    break;
                case "÷":
                    if (secondValue != 0) {
                        result = selectedNumber / secondValue;
                    }
                    break;
                case "%":
                    result = (selectedNumber / 100) * secondValue;
                    break;
            }

            calculatorTextView.setText(String.valueOf(result));
            selectedNumber = result;
        }
    }

    public void onClearClick(View v) {
        calculatorTextView.setText("");
        selectedNumber = 0.0;
    }

    public void onDeleteClick(View v) {
        String currentText = calculatorTextView.getText().toString();
        calculatorTextView.setText(currentText.substring(0, currentText.length() - 1));
    }
}