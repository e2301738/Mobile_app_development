package fi.christian.assignment_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    private TextView resultsTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initializeViews();
        displayResults();
        setupListeners();
    }

    private void initializeViews() {
        resultsTextView = findViewById(R.id.resultsTextView);
        backButton = findViewById(R.id.backButton);
    }

    private void setupListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayResults() {
        ArrayList<Meeting> meetings = MeetingManager.getMeetings();
        StringBuilder builder = new StringBuilder();
        for (Meeting meeting : meetings) {
            builder.append(meeting.toString()).append("\n\n");
        }
        
        if (meetings.isEmpty()) {
            resultsTextView.setText(getString(R.string.no_meetings_text));
        } else {
            resultsTextView.setText(builder.toString());
        }
    }
}
