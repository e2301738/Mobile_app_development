package fi.christian.assignment_7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText titleEditText, placeEditText, participantsEditText, dateEditText, timeEditText;
    private Button submitButton, summaryButton, searchButton, updateButton;
    private Drawable titleBackground, placeBackground, participantsBackground, dateBackground, timeBackground;

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
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        submitButton = findViewById(R.id.submitButton);
        summaryButton = findViewById(R.id.summaryButton);
        searchButton = findViewById(R.id.searchButton);
        updateButton = findViewById(R.id.updateButton);

        titleBackground = titleEditText.getBackground();
        placeBackground = placeEditText.getBackground();
        participantsBackground = participantsEditText.getBackground();
        dateBackground = dateEditText.getBackground();
        timeBackground = timeEditText.getBackground();

        setupTextWatcher(titleEditText, titleBackground);
        setupTextWatcher(placeEditText, placeBackground);
        setupTextWatcher(participantsEditText, participantsBackground);
        setupTextWatcher(dateEditText, dateBackground);
        setupTextWatcher(timeEditText, timeBackground);

        dateEditText.setFocusable(false);
        dateEditText.setClickable(true);
        timeEditText.setFocusable(false);
        timeEditText.setClickable(true);
    }

    private void setupTextWatcher(final EditText editText, final Drawable originalBackground) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText.setBackground(originalBackground);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setupListeners() {
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSummaryActivity(true);
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSummaryActivity(false);
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

    private boolean isInputValid(String title, String place, String participants, String date, String time) {
        boolean isValid = true;

        if (time.isEmpty()) {
            timeEditText.setBackgroundColor(Color.RED);
            isValid = false;
        }
        if (date.isEmpty()) {
            dateEditText.setBackgroundColor(Color.RED);
            isValid = false;
        }
        if (participants.isEmpty()) {
            participantsEditText.setBackgroundColor(Color.RED);
            participantsEditText.requestFocus();
            isValid = false;
        }
        if (place.isEmpty()) {
            placeEditText.setBackgroundColor(Color.RED);
            placeEditText.requestFocus();
            isValid = false;
        }
        if (title.isEmpty()) {
            titleEditText.setBackgroundColor(Color.RED);
            titleEditText.requestFocus();
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void startSummaryActivity(boolean isSubmit) {
        if (isSubmit) {
            String title = titleEditText.getText().toString();
            String place = placeEditText.getText().toString();
            String participants = participantsEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();

            if (!isInputValid(title, place, participants, date, time)) {
                return;
            }

            Meeting meeting = new Meeting(title, place, participants, date, time);
            MeetingManager.addMeeting(meeting);
            Toast.makeText(this, getString(R.string.toast_meeting_added), Toast.LENGTH_SHORT).show();
            clearFields();
        }

        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }

    private void clearFields() {
        titleEditText.setText("");
        placeEditText.setText("");
        participantsEditText.setText("");
        dateEditText.setText("");
        timeEditText.setText("");
        titleEditText.requestFocus();
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
