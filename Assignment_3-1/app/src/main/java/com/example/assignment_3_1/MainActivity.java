package com.example.assignment_3_1;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BlogEntryHandler entryHandler;
    private TextView displayBlogPostTextView;
    private RadioGroup searchRadioGroup;
    private RadioButton textSearchRadioButton;
    private EditText userNameEditText, commentEditText, textSearchEditText;
    private DatePicker datePicker;
    private Button searchButton, submitButton, refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        entryHandler = new BlogEntryHandler();

        displayBlogPostTextView = findViewById(R.id.textDisplay);
        userNameEditText = findViewById(R.id.userName);
        commentEditText = findViewById(R.id.commentField);
        searchRadioGroup = findViewById(R.id.searchRadioGroup);
        textSearchRadioButton = findViewById(R.id.textSearchRadio);
        textSearchRadioButton.setChecked(true);
        textSearchEditText = findViewById(R.id.textSearchField);
        datePicker = findViewById(R.id.calendarSearch);
        submitButton = findViewById(R.id.submitButton);
        searchButton = findViewById(R.id.searchButton);
        refreshButton = findViewById(R.id.refreshButton);

        TextWatcher colorResetter = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userNameEditText.hasFocus()) {
                    userNameEditText.setBackgroundColor(Color.TRANSPARENT);
                }
                else if (commentEditText.hasFocus()) {
                    commentEditText.setBackgroundColor(Color.TRANSPARENT);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        userNameEditText.addTextChangedListener(colorResetter);
        commentEditText.addTextChangedListener(colorResetter);



        searchRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.textSearchRadio) {
                textSearchEditText.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
            } else if (checkedId == R.id.dateSearchRadio) {
                textSearchEditText.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String comment = commentEditText.getText().toString().trim();

                if (userName.isEmpty() || comment.isEmpty()) {
                    if (userName.isEmpty()){
                        userNameEditText.setBackgroundColor(Color.RED);
                    }
                    if (comment.isEmpty()){
                        commentEditText.setBackgroundColor(Color.RED);
                    }
                }
                else{
                    entryHandler.addBlogEntry(new BlogEntry(comment, userName));
                    displayBlogEntries(entryHandler.getAllBlogEntries());

                    userNameEditText.setText("");
                    commentEditText.setText("");
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSearchRadioButton.isChecked()) {
                    displayBlogEntries(entryHandler.filterByText(textSearchEditText.getText().toString()));
                    textSearchEditText.setText("");
                } else {
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();

                    String inputDate = String.format(Locale.getDefault(), "%02d.%02d.%d", month, day, year);
                    displayBlogEntries(entryHandler.filterByDate(inputDate));
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSearchEditText.setText("");
                textSearchRadioButton.setChecked(true);
                displayBlogEntries(entryHandler.getAllBlogEntries());
            }
        });

    }

    private void displayBlogEntries(List<BlogEntry> blogEntries) {
        StringBuilder stringBuilder = new StringBuilder();
        for (BlogEntry entry : blogEntries){
            stringBuilder.append(entry.toString()).append("\n\n");
        }
        displayBlogPostTextView.setText(stringBuilder.toString());
    }

}