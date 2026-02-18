package com.example.assignment_3_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TextView calculatorTextView;
    private double firstSelectedValue = 0.0;
    private String currentOperator, additionText, subtractionText, multiplicationText, divisionText, percentText;
    private boolean isNewOperation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculatorTextView = findViewById(R.id.calcDisplay);
        additionText = getString(R.string.addition);
        subtractionText = getString(R.string.subtraction);
        multiplicationText = getString(R.string.multiplication);
        divisionText = getString(R.string.division);
        percentText = getString(R.string.percent);
    }

    public void onNumberClick(View v) {
        Button numberButton = (Button) v;

        if (isNewOperation){
            calculatorTextView.setText("");
            isNewOperation = false;
        }

        calculatorTextView.append(numberButton.getText().toString());
    }

    public void onOperatorClick(View v) {
        String currentCalculatorValue = calculatorTextView.getText().toString();

        if (!currentCalculatorValue.isEmpty()) {
            firstSelectedValue = Double.parseDouble(currentCalculatorValue);
            currentOperator = ((Button) v).getText().toString();
            isNewOperation = true;
        }
    }

    public void onEqualClick(View v) {
        String currentCalculatorValue = calculatorTextView.getText().toString();

        if(!currentCalculatorValue.isEmpty()){
            double secondSelectedValue = Double.parseDouble(currentCalculatorValue);
            double result = 0.0;

            if (currentOperator.equals(additionText)) {
                result = firstSelectedValue + secondSelectedValue;
            }
            else if (currentOperator.equals(subtractionText)) {
                result = firstSelectedValue - secondSelectedValue;
            }
            else if (currentOperator.equals(multiplicationText)) {
                result = firstSelectedValue * secondSelectedValue;
            }
            else if (currentOperator.equals(divisionText)) {
                if (firstSelectedValue != 0 && secondSelectedValue != 0) {
                    result = firstSelectedValue / secondSelectedValue;
                } else {
                    calculatorTextView.setText(R.string.divide_with_0);
                    isNewOperation = true;
                    return;
                }
            }
            else if (currentOperator.equals(percentText)) {
                result = (firstSelectedValue / 100) * secondSelectedValue;
            }
            calculatorTextView.setText(String.format("%.2f", result));
            firstSelectedValue = result;
            currentOperator = "";
            isNewOperation = true;
        }
    }

    public void onBackSpaceClick(View v) {
        String currentNumberOnDisplay = calculatorTextView.getText().toString();
        if(!currentNumberOnDisplay.isEmpty()){
            calculatorTextView.setText(currentNumberOnDisplay.substring(0, currentNumberOnDisplay.length() - 1));
        }
    }

    public void onClearClick(View v){
        currentOperator = "";
        firstSelectedValue = 0.0;
        calculatorTextView.setText("");
    }
}