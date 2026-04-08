package fi.christian.meeting_calendar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    private EditText searchTitleEditText, titleEditText, placeEditText;
    private TextView participantsDisplayTextView;
    private Button searchDateButton, dateButton, timeButton, addParticipantsButton, searchButton, clearSearchButton, saveButton, deleteButton;
    private ImageButton backButton;
    private LinearLayout editFieldsContainer;
    private RecyclerView searchResultsRecyclerView;
    private MeetingAdapter meetingAdapter;
    private final ArrayList<Meeting> searchResultsList = new ArrayList<>();
    private final ArrayList<Integer> indexList = new ArrayList<>();
    private int selectedIndex;
    private Drawable titleBackground, placeBackground;
    private ArrayList<String> selectedParticipants = new ArrayList<>();

    private ActivityResultLauncher<Intent> participantsActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        participantsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedParticipants = result.getData().getStringArrayListExtra("participants");
                            updateParticipantsDisplay();
                            addParticipantsButton.setTextColor(ThemeManager.getFontColor(UpdateActivity.this));
                        }
                    }
                });

        initializeViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.updateLayout));
    }

    private void initializeViews() {
        searchTitleEditText = findViewById(R.id.searchTitleEditText);
        searchDateButton = findViewById(R.id.searchDateButton);
        searchButton = findViewById(R.id.searchButton);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        titleEditText = findViewById(R.id.titleEditText);
        placeEditText = findViewById(R.id.placeEditText);
        addParticipantsButton = findViewById(R.id.addParticipantsButton);
        participantsDisplayTextView = findViewById(R.id.participantsDisplayTextView);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        editFieldsContainer = findViewById(R.id.editFieldsContainer);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        titleBackground = titleEditText.getBackground();
        placeBackground = placeEditText.getBackground();

        InputHandler.setupTextWatcher(titleEditText, titleBackground);
        InputHandler.setupTextWatcher(placeEditText, placeBackground);

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        meetingAdapter = new MeetingAdapter(searchResultsList, new MeetingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Meeting meeting) {
                selectedIndex = indexList.get(position);
                populateFields(meeting);
                editFieldsContainer.setVisibility(View.VISIBLE);
                searchResultsRecyclerView.setVisibility(View.GONE);
                ThemeManager.applyTheme(UpdateActivity.this, editFieldsContainer);
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

        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, ParticipantsActivity.class);
                intent.putStringArrayListExtra("participants", selectedParticipants);
                participantsActivityResultLauncher.launch(intent);
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

    private void updateParticipantsDisplay() {
        int fontColor = ThemeManager.getFontColor(this);
        if (selectedParticipants == null || selectedParticipants.isEmpty()) {
            participantsDisplayTextView.setText(R.string.no_participants_selected);
            participantsDisplayTextView.setTextColor(fontColor & 0x80FFFFFF);
        } else {
            participantsDisplayTextView.setText(String.join(", ", selectedParticipants));
            participantsDisplayTextView.setTextColor(fontColor);
        }
    }

    private void performSearch() {
        String titleSearch = searchTitleEditText.getText().toString().toLowerCase().trim();
        String dateSearch = searchDateButton.getText().toString().trim();
        String dateHint = getString(R.string.update_date_search_hint);

        indexList.clear();
        indexList.addAll(MeetingManager.getMeetingIndex(titleSearch, dateSearch, dateHint));

        searchResultsList.clear();
        ArrayList<Meeting> allMeetings = MeetingManager.getMeetings();
        for (int index : indexList) {
            searchResultsList.add(allMeetings.get(index));
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
            ThemeManager.applyTheme(this, editFieldsContainer);
        } else {
            meetingAdapter.updateList(searchResultsList);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            editFieldsContainer.setVisibility(View.GONE);
            ThemeManager.applyTheme(this, searchResultsRecyclerView);
        }
    }

    private void performUpdate() {
        String title = titleEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String date = dateButton.getText().toString();
        String time = timeButton.getText().toString();

        if (!isInputValid()) {
            return;
        }

        Meeting meeting = new Meeting(title, place, new ArrayList<>(selectedParticipants), date, time);
        MeetingManager.updateMeeting(selectedIndex, meeting);

        toastMessage(getString(R.string.toast_update_success));
        finish();
    }

    private boolean isInputValid() {
        boolean isValid = true;

        if (!InputHandler.validateParticipants(participantsDisplayTextView, addParticipantsButton)) {
            isValid = false;
        }

        if (!InputHandler.validateInputIsEmpty(placeEditText)) isValid = false;
        if (!InputHandler.validateInputIsEmpty(titleEditText)) isValid = false;

        if (!isValid) {
            toastMessage(getString(R.string.fill_fields));
        }

        return isValid;
    }

    private void performDelete() {
        MeetingManager.deleteMeeting(selectedIndex);
        toastMessage(getString(R.string.toast_delete_success));
        finish();
    }

    private void populateFields(Meeting meeting) {
        titleEditText.setText(meeting.getTitle());
        placeEditText.setText(meeting.getPlace());
        selectedParticipants = new ArrayList<>(meeting.getParticipants());
        updateParticipantsDisplay();
        addParticipantsButton.setTextColor(ThemeManager.getFontColor(this));
        dateButton.setText(meeting.getDate());
        timeButton.setText(meeting.getTime());
        
        titleEditText.setBackground(titleBackground);
        placeEditText.setBackground(placeBackground);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
