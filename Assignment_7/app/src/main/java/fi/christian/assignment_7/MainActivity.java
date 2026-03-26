package fi.christian.assignment_7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText titleEditText, placeEditText, participantsEditText;
    private Button dateButton, timeButton, submitButton, summaryButton, searchButton, updateButton;
    private Drawable titleBackground, placeBackground, participantsBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MeetingManager.initializeMockData();
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.titleEditText);
        placeEditText = findViewById(R.id.placeEditText);
        participantsEditText = findViewById(R.id.participantsEditText);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        submitButton = findViewById(R.id.submitButton);
        summaryButton = findViewById(R.id.summaryButton);
        searchButton = findViewById(R.id.searchButton);
        updateButton = findViewById(R.id.updateButton);

        titleBackground = titleEditText.getBackground();
        placeBackground = placeEditText.getBackground();
        participantsBackground = participantsEditText.getBackground();

        InputHandler.setupTextWatcher(titleEditText, titleBackground);
        InputHandler.setupTextWatcher(placeEditText, placeBackground);
        InputHandler.setupTextWatcher(participantsEditText, participantsBackground);
    }

    private void setupListeners() {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMeeting();
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSummaryActivity();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivity();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateActivity();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    private void showTimePicker() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    private void submitMeeting() {
        if (!isInputValid()) {
            return;
        }

        String title = titleEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String participants = participantsEditText.getText().toString();
        String date = dateButton.getText().toString();
        String time = timeButton.getText().toString();

        Meeting meeting = new Meeting(title, place, participants, date, time);
        MeetingManager.addMeeting(meeting);
        Toast.makeText(this, getString(R.string.toast_meeting_added), Toast.LENGTH_SHORT).show();
        clearFields();
        MeetingManager.sortMeetings();
        startSummaryActivity();
    }

    private boolean isInputValid() {
        boolean isValid = true;

        if (!InputHandler.validatePickedDateAndTime(timeButton, getString(R.string.meeting_time_hint))) {
            isValid = false;
        }
        if (!InputHandler.validatePickedDateAndTime(dateButton, getString(R.string.meeting_date_hint))) {
            isValid = false;
        }
        if (!InputHandler.validateInputIsEmpty(participantsEditText)) {
            isValid = false;
        }
        if (!InputHandler.validateInputIsEmpty(placeEditText)) {
            isValid = false;
        }
        if (!InputHandler.validateInputIsEmpty(titleEditText)) {
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private void clearFields() {
        titleEditText.setText("");
        placeEditText.setText("");
        participantsEditText.setText("");
        dateButton.setText(R.string.meeting_date_hint);
        dateButton.setTextColor(Color.BLACK);
        timeButton.setText(R.string.meeting_time_hint);
        timeButton.setTextColor(Color.BLACK);
        titleEditText.requestFocus();
    }

    private void startSummaryActivity() {
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void startUpdateActivity() {
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
    }
}
