package fi.christian.assignment_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton, backButton;
    private RecyclerView searchResultsRecyclerView;
    private TextView noResultsTextView;
    private MeetingAdapter meetingAdapter;
    private ArrayList<Meeting> resultsList = new ArrayList<>();

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
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        noResultsTextView = findViewById(R.id.noResultsTextView);

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        meetingAdapter = new MeetingAdapter(resultsList, null);
        searchResultsRecyclerView.setAdapter(meetingAdapter);
    }

    private void setupListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString().toLowerCase().trim();
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
        resultsList.clear();
        resultsList.addAll(MeetingManager.searchMeetings(searchString));

        if (resultsList.isEmpty()) {
            noResultsTextView.setVisibility(View.VISIBLE);
            searchResultsRecyclerView.setVisibility(View.GONE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            meetingAdapter.updateList(resultsList);
        }
    }
}
