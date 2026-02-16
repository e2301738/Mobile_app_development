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
    private EditText userNameEditText, commentEditText,textSearchFieldEditText;
    private DatePicker datePicker;
    private Button searchButton, submitButton;

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
        textSearchFieldEditText = findViewById(R.id.textSearchField);
        datePicker = findViewById(R.id.calendarSearch);
        submitButton = findViewById(R.id.submitButton);
        searchButton = findViewById(R.id.searchButton);

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
                textSearchFieldEditText.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
            } else if (checkedId == R.id.dateSearchRadio) {
                textSearchFieldEditText.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userNameEditText.getText().toString().trim();
                String comment = commentEditText.getText().toString().trim();

                if (name.isEmpty()) {
                    userNameEditText.setBackgroundColor(Color.RED);
                    if(comment.isEmpty()){
                        commentEditText.setBackgroundColor(Color.RED);
                    }
                }
                else if (comment.isEmpty()) {
                    commentEditText.setBackgroundColor(Color.RED);
                }
                else{
                    entryHandler.addBlogEntry(new BlogEntry(comment, name));
                    displayBlogEntries(entryHandler.getAllBlogEntries());

                    userNameEditText.setText("");
                    commentEditText.setText("");
                }
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BlogEntry> searchResult;
                if (textSearchRadioButton.isChecked()) {
                    searchResult = entryHandler.filterByText(textSearchFieldEditText.getText().toString());
                    textSearchFieldEditText.setText("");
                } else {
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();

                    String date = String.format(Locale.getDefault(), "%02d.%02d.%d", month, day, year);
                    searchResult = entryHandler.filterByDate(date);
                }
                displayBlogEntries(searchResult);
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