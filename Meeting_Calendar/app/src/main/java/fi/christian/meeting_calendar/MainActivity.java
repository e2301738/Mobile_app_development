package fi.christian.meeting_calendar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText titleEditText, placeEditText;
    private TextView participantsDisplayTextView;
    private Button dateButton, timeButton, addParticipantsButton, submitButton, summaryButton, searchButton, updateButton;
    private ImageButton settingsButton;
    private Drawable titleBackground, placeBackground;
    private ArrayList<Participant> selectedParticipants = new ArrayList<>();
    private ActivityResultLauncher<Intent> participantsActivityResultLauncher;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(this);

        participantsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            selectedParticipants.clear();
                            selectedParticipants.addAll(MeetingManager.getTempParticipants());
                            updateParticipantsDisplay();
                            addParticipantsButton.setTextColor(ThemeManager.getFontColor(MainActivity.this));
                        }
                    }
                });

        initializeViews();
        setupListeners();
        
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        ArrayList<Meeting> meetings = dbAdapter.getAllMeetings();
        MeetingManager.setMeetings(meetings);
        
        ArrayList<Participant> allParticipants = dbAdapter.getAllParticipants();
        MeetingManager.setAllParticipants(allParticipants);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.mainLayout));
        loadFromDatabase();
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.titleEditText);
        placeEditText = findViewById(R.id.placeEditText);
        participantsDisplayTextView = findViewById(R.id.participantsDisplayTextView);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        addParticipantsButton = findViewById(R.id.addParticipantsButton);
        submitButton = findViewById(R.id.submitButton);
        summaryButton = findViewById(R.id.summaryButton);
        searchButton = findViewById(R.id.searchButton);
        updateButton = findViewById(R.id.updateButton);
        settingsButton = findViewById(R.id.settingsButton);

        titleBackground = titleEditText.getBackground();
        placeBackground = placeEditText.getBackground();

        InputHandler.setupTextWatcher(titleEditText, titleBackground);
        InputHandler.setupTextWatcher(placeEditText, placeBackground);
    }

    private void setupListeners() {
        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startParticipantsActivity();
            }
        });

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

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsMenu(v);
            }
        });
    }

    private void showSettingsMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.settings_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_theme) {
                    startThemeActivity();
                    return true;
                } else if (itemId == R.id.action_manage_participants) {
                    startAllParticipantsActivity();
                    return true;
                } else if (itemId == R.id.action_write_to_file) {
                    startFileActivity();
                    return true;
                } else if (itemId == R.id.action_about) {
                    startAboutActivity();
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void startParticipantsActivity() {
        Intent intent = new Intent(this, ParticipantsActivity.class);
        MeetingManager.setTempParticipants(new ArrayList<>(selectedParticipants));
        participantsActivityResultLauncher.launch(intent);
    }

    private void startAllParticipantsActivity() {
        Intent intent = new Intent(this, AllParticipantsActivity.class);
        startActivity(intent);
    }

    private void startThemeActivity() {
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivity(intent);
    }

    private void startFileActivity() {
        Intent intent = new Intent(this, FileActivity.class);
        startActivity(intent);
    }

    private void startAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void updateParticipantsDisplay() {
        if (selectedParticipants == null || selectedParticipants.isEmpty()) {
            participantsDisplayTextView.setText(R.string.no_participants_selected);
        } else {
            ArrayList<String> names = new ArrayList<>();
            for (Participant p : selectedParticipants) names.add(p.getName());
            participantsDisplayTextView.setText(TextUtils.join(", ", names));
        }
        participantsDisplayTextView.setTextColor(ThemeManager.getFontColor(this));
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
        String date = dateButton.getText().toString();
        String time = timeButton.getText().toString();

        Meeting meeting = new Meeting(title, place, new ArrayList<>(selectedParticipants), date, time);
        dbAdapter.addMeeting(meeting);

        loadFromDatabase();
        
        clearFields();
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
        if (!InputHandler.validateParticipants(participantsDisplayTextView, addParticipantsButton)) {
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
        selectedParticipants.clear();
        MeetingManager.clearTempParticipants();
        updateParticipantsDisplay();
        addParticipantsButton.setTextColor(ThemeManager.getFontColor(this));
        dateButton.setText(R.string.meeting_date_hint);
        dateButton.setTextColor(ThemeManager.getFontColor(this));
        timeButton.setText(R.string.meeting_time_hint);
        timeButton.setTextColor(ThemeManager.getFontColor(this));
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
