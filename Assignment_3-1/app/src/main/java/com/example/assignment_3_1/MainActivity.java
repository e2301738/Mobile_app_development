package com.example.assignment_3_1;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    BlogEntry entry;
    private TextView tvDisplayBlogPost;
    private RadioGroup searchGroup;
    private EditText textSearchField;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        entry = new BlogEntry("test", "Christian");
        String theEntry = entry.toString();

        tvDisplayBlogPost = findViewById(R.id.textDisplay);
        tvDisplayBlogPost.setText(theEntry);

        searchGroup = findViewById(R.id.searchRadioGroup);
        textSearchField = findViewById(R.id.textSearchField);
        datePicker = findViewById(R.id.calendarSearch);


        searchGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.textSearchRadio) {
                    textSearchField.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.GONE);
                } else if (checkedId == R.id.dateSearchRadio) {
                    textSearchField.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
