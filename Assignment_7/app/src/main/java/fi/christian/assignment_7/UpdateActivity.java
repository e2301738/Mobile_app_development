package fi.christian.assignment_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    private EditText searchTitleEditText, searchDateEditText, titleEditText, placeEditText, participantsEditText, dateEditText, timeEditText;
    private Button searchButton, clearSearchButton, saveButton, deleteButton, backButton;
    private LinearLayout editFieldsContainer;
    private RecyclerView searchResultsRecyclerView;
    
    private MeetingAdapter Meetingadapter;
    private ArrayList<Meeting> searchResultsList = new ArrayList<>();
    private ArrayList<Integer> actualIndices = new ArrayList<>();
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        searchTitleEditText = findViewById(R.id.searchTitleEditText);
        searchDateEditText = findViewById(R.id.searchDateEditText);
        searchButton = findViewById(R.id.searchButton);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        titleEditText = findViewById(R.id.titleEditText);
        placeEditText = findViewById(R.id.placeEditText);
        participantsEditText = findViewById(R.id.participantsEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        editFieldsContainer = findViewById(R.id.editFieldsContainer);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        searchDateEditText.setFocusable(false);
        searchDateEditText.setClickable(true);
        dateEditText.setFocusable(false);
        dateEditText.setClickable(true);
        timeEditText.setFocusable(false);
        timeEditText.setClickable(true);

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Meetingadapter = new MeetingAdapter(searchResultsList, new MeetingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Meeting meeting) {
                selectedIndex = actualIndices.get(position);
                populateFields(meeting);
                editFieldsContainer.setVisibility(View.VISIBLE);
                searchResultsRecyclerView.setVisibility(View.GONE);
            }
        });
        searchResultsRecyclerView.setAdapter(Meetingadapter);
    }

    private void setupListeners() {
        searchDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "SearchDatePicker");
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTitleEditText.setText("");
                searchDateEditText.setText("");
                searchResultsRecyclerView.setVisibility(View.GONE);
                editFieldsContainer.setVisibility(View.GONE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUpdate();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDelete();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void performSearch() {
        String titleSearch = searchTitleEditText.getText().toString().trim();
        String dateSearch = searchDateEditText.getText().toString().trim();

        if (titleSearch.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_search_title), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Meeting> foundMeetings = new ArrayList<>();
        actualIndices.clear();
        ArrayList<Meeting> allMeetings = MeetingManager.getMeetings();

        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting meeting = allMeetings.get(i);
            boolean titleMatches = meeting.getTitle().equals(titleSearch);
            boolean dateMatches = dateSearch.isEmpty() || meeting.getDate().equals(dateSearch);

            if (titleMatches && dateMatches) {
                foundMeetings.add(meeting);
                actualIndices.add(i);
            }
        }

        if (foundMeetings.isEmpty()) {
            Toast.makeText(this, getString(R.string.meeting_not_found), Toast.LENGTH_SHORT).show();
            searchResultsRecyclerView.setVisibility(View.GONE);
            editFieldsContainer.setVisibility(View.GONE);
        } else if (foundMeetings.size() == 1) {
            selectedIndex = actualIndices.get(0);
            populateFields(foundMeetings.get(0));
            editFieldsContainer.setVisibility(View.VISIBLE);
            searchResultsRecyclerView.setVisibility(View.GONE);
        } else {
            Meetingadapter.updateList(foundMeetings);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            editFieldsContainer.setVisibility(View.GONE);
        }
    }

    private void performUpdate() {
        String title = titleEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String participants = participantsEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();

        Meeting meeting = new Meeting(title, place, participants, date, time);
        MeetingManager.updateMeeting(selectedIndex, meeting);
        
        Toast.makeText(this, getString(R.string.toast_update_success), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void performDelete() {
        MeetingManager.deleteMeeting(selectedIndex);
        Toast.makeText(this, getString(R.string.toast_delete_success), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void populateFields(Meeting meeting) {
        titleEditText.setText(meeting.getTitle());
        placeEditText.setText(meeting.getPlace());
        participantsEditText.setText(meeting.getParticipants());
        dateEditText.setText(meeting.getDate());
        timeEditText.setText(meeting.getTime());
    }
}
