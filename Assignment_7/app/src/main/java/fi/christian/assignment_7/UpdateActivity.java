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

    private EditText searchTitleEditText, titleEditText, placeEditText, participantsEditText;
    private Button searchDateButton, dateButton, timeButton, searchButton, clearSearchButton, saveButton, deleteButton, backButton;
    private LinearLayout editFieldsContainer;
    private RecyclerView searchResultsRecyclerView;
    private MeetingAdapter meetingAdapter;
    private final ArrayList<Meeting> searchResultsList = new ArrayList<>();
    private final ArrayList<Integer> indexList = new ArrayList<>();
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
        searchDateButton = findViewById(R.id.searchDateButton);
        searchButton = findViewById(R.id.searchButton);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        titleEditText = findViewById(R.id.titleEditText);
        placeEditText = findViewById(R.id.placeEditText);
        participantsEditText = findViewById(R.id.participantsEditText);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        editFieldsContainer = findViewById(R.id.editFieldsContainer);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        meetingAdapter = new MeetingAdapter(searchResultsList, new MeetingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Meeting meeting) {
                selectedIndex = indexList.get(position);
                populateFields(meeting);
                editFieldsContainer.setVisibility(View.VISIBLE);
                searchResultsRecyclerView.setVisibility(View.GONE);
            }
        });
        searchResultsRecyclerView.setAdapter(meetingAdapter);
    }

    private void setupListeners() {
        searchDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "SearchDatePicker");
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "UpdateDatePicker");
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "UpdateTimePicker");
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
                searchDateButton.setText(R.string.update_date_search_hint);
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
        String titleSearch = searchTitleEditText.getText().toString().toLowerCase().trim();
        String dateSearch = searchDateButton.getText().toString().trim();
        String dateHint = getString(R.string.update_date_search_hint);

        searchResultsList.clear();
        indexList.clear();
        ArrayList<Meeting> allMeetings = MeetingManager.getMeetings();

        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting meeting = allMeetings.get(i);
            boolean titleMatches = meeting.getTitle().toLowerCase().contains(titleSearch);
            boolean dateMatches = dateSearch.equals(dateHint) || meeting.getDate().equals(dateSearch);

            if (titleMatches && dateMatches) {
                searchResultsList.add(meeting);
                indexList.add(i);
            }
        }

        if (searchResultsList.isEmpty()) {
            toastMessage(getString(R.string.meeting_not_found));
            searchResultsRecyclerView.setVisibility(View.GONE);
            editFieldsContainer.setVisibility(View.GONE);
        } else if (searchResultsList.size() == 1) {
            selectedIndex = indexList.get(0);
            populateFields(searchResultsList.get(0));
            editFieldsContainer.setVisibility(View.VISIBLE);
            searchResultsRecyclerView.setVisibility(View.GONE);
        } else {
            meetingAdapter.updateList(searchResultsList);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            editFieldsContainer.setVisibility(View.GONE);
        }
    }

    private void performUpdate() {
        String title = titleEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String participants = participantsEditText.getText().toString();
        String date = dateButton.getText().toString();
        String time = timeButton.getText().toString();

        Meeting meeting = new Meeting(title, place, participants, date, time);
        MeetingManager.updateMeeting(selectedIndex, meeting);

        toastMessage(getString(R.string.toast_update_success));
        finish();
    }

    private void performDelete() {
        MeetingManager.deleteMeeting(selectedIndex);
        toastMessage(getString(R.string.toast_delete_success));
        finish();
    }

    private void populateFields(Meeting meeting) {
        titleEditText.setText(meeting.getTitle());
        placeEditText.setText(meeting.getPlace());
        participantsEditText.setText(meeting.getParticipants());
        dateButton.setText(meeting.getDate());
        timeButton.setText(meeting.getTime());
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
