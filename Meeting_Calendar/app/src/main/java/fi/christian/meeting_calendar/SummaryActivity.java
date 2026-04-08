package fi.christian.meeting_calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    private RecyclerView summaryRecyclerView;
    private TextView noMeetingsTextView;
    private ImageButton backButton;
    private MeetingAdapter meetingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initializeViews();
        displayResults();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.summaryLayout));
    }

    private void initializeViews() {
        summaryRecyclerView = findViewById(R.id.summaryRecyclerView);
        noMeetingsTextView = findViewById(R.id.noMeetingsTextView);
        backButton = findViewById(R.id.backButton);

        summaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        meetingAdapter = new MeetingAdapter(MeetingManager.getMeetings(), null);
        summaryRecyclerView.setAdapter(meetingAdapter);
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
        
        if (meetings.isEmpty()) {
            noMeetingsTextView.setVisibility(View.VISIBLE);
            summaryRecyclerView.setVisibility(View.GONE);
        } else {
            noMeetingsTextView.setVisibility(View.GONE);
            summaryRecyclerView.setVisibility(View.VISIBLE);
            meetingAdapter.updateList(meetings);
        }
    }
}
