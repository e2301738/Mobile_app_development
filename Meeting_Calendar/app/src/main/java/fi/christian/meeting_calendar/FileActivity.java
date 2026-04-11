package fi.christian.meeting_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;

public class FileActivity extends AppCompatActivity {

    private RadioButton internalRadioButton, externalRadioButton;
    private Button saveToFileButton, readFromFileButton, mergeDataButton;
    private ImageButton backButton, settingsButton;
    private String destinationPath, fileName;
    private File destinationDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_rw);

        fileName = getString(R.string.meeting_file_name);
        destinationPath = getString(R.string.meetings_data_dir);

        initializeViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.fileWriteLayout));
    }

    private void initializeViews() {
        internalRadioButton = findViewById(R.id.radioInternal);
        externalRadioButton = findViewById(R.id.radioExternal);
        saveToFileButton = findViewById(R.id.btnSaveToFile);
        readFromFileButton = findViewById(R.id.btnReadFromFile);
        mergeDataButton = findViewById(R.id.btnMergeData);
        backButton = findViewById(R.id.backButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    private void setupListeners() {
        saveToFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeetingsToFile();
            }
        });

        readFromFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeetings(false);
            }
        });

        mergeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeetings(true);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    finish();
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void startThemeActivity() {
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivity(intent);
    }

    private void updateDestinationDirectory() {
        if (internalRadioButton.isChecked()) {
            destinationDirectory = new File(getFilesDir(), destinationPath);
        } else {
            destinationDirectory = getExternalFilesDir(destinationPath);
        }

        if (destinationDirectory != null && !destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }
    }

    private void saveMeetingsToFile() {
        updateDestinationDirectory();
        if (destinationDirectory == null) {
            Toast.makeText(this, R.string.storage_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = FileManager.saveMeetings(this, destinationDirectory, fileName, MeetingManager.getMeetings());

        if (success) {
            int toastResourceId = externalRadioButton.isChecked() ? R.string.toast_file_written_sd : R.string.toast_file_written_phone;
            File file = new File(destinationDirectory, fileName);
            Toast.makeText(this, getString(toastResourceId) + ": " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.io_excp), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMeetings(boolean merge) {
        updateDestinationDirectory();
        if (!merge) {
            if (FileManager.loadMeetings(this, destinationDirectory, fileName, false)) {
                ThemeManager.applyTheme(this, findViewById(R.id.fileWriteLayout));
                Toast.makeText(this, getString(R.string.file_load_fb), Toast.LENGTH_SHORT).show();
            } else {
                handleLoadError();
            }
        } else {


            FileManager.loadMeetings(this, destinationDirectory, fileName, true);
            JSONObject targetSettings = FileManager.getSettingsFromFile(this, destinationDirectory, fileName);
            boolean success = FileManager.saveMeetings(this, destinationDirectory, fileName, MeetingManager.getMeetings(), targetSettings);
            
            if (success) {
                Toast.makeText(this, getString(R.string.toast_merge_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.io_excp), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleLoadError() {
        File file = new File(destinationDirectory, fileName);
        if (!file.exists()) {
            Toast.makeText(this, getString(R.string.file_not_found_excp), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.io_excp), Toast.LENGTH_SHORT).show();
        }
    }
}
