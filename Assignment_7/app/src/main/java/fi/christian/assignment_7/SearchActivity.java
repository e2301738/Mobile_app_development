package fi.christian.assignment_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton, backButton;
    private TextView resultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        backButton = findViewById(R.id.backButton);
        resultsTextView = findViewById(R.id.resultsTextView);
    }

    private void setupListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString().trim();
                displayResults(searchString);
                searchEditText.setText("");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayResults(String searchString) {
        ArrayList<Meeting> results = MeetingManager.searchMeetings(searchString);

        if (results.isEmpty()) {
            resultsTextView.setText(getString(R.string.no_matches_found));
        } else {
            StringBuilder builder = new StringBuilder();
            for (Meeting meeting : results) {
                builder.append(meeting.toString()).append("\n\n");
            }
            resultsTextView.setText(builder.toString());
        }
    }
}
