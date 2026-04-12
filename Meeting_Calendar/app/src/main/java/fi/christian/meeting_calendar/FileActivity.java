package fi.christian.meeting_calendar;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private Button saveToFileButton, readFromFileButton, mergeDataButton, deleteStorageButton;
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

    private void saveStoragePreference() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_app_state), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(getString(R.string.key_last_storage_type), internalRadioButton.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_app_state), MODE_PRIVATE);
        boolean useInternal = prefs.getBoolean(getString(R.string.key_last_storage_type), true);
        if (useInternal) {
            internalRadioButton.setChecked(true);
        } else {
            externalRadioButton.setChecked(true);
        }
        
        updateDestinationDirectory();
        ThemeManager.applyTheme(this, findViewById(R.id.fileWriteLayout));
    }

    private void initializeViews() {
        internalRadioButton = findViewById(R.id.radioInternal);
        externalRadioButton = findViewById(R.id.radioExternal);
        saveToFileButton = findViewById(R.id.saveToFileButton);
        readFromFileButton = findViewById(R.id.readFromFileButton);
        mergeDataButton = findViewById(R.id.mergeDataButton);
        deleteStorageButton = findViewById(R.id.deleteStorageButton);
        backButton = findViewById(R.id.backButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    private void setupListeners() {
        internalRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDestinationDirectory();
            }
        });

        externalRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDestinationDirectory();
            }
        });

        saveToFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveToSelectedStorage();
            }
        });

        readFromFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoadOrMergeFromStorage(false);
            }
        });

        mergeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoadOrMergeFromStorage(true);
            }
        });

        deleteStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeleteStorageFile();
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

    private void handleSaveToSelectedStorage() {
        updateDestinationDirectory();
        
        boolean success = FileManager.writeMeetingsAndSettingsToFile(this, destinationDirectory, fileName, MeetingManager.getMeetings(), null);

        if (success) {
            saveStoragePreference();
            int toastResourceId = externalRadioButton.isChecked() ? R.string.toast_file_written_sd : R.string.toast_file_written_phone;
            File file = new File(destinationDirectory, fileName);
            Toast.makeText(this, getString(toastResourceId) + ": " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.io_excp), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLoadOrMergeFromStorage(boolean merge) {
        updateDestinationDirectory();

        if (!merge) {
            if (FileManager.readMeetingsAndSettingsFromFile(this, destinationDirectory, fileName, false)) {
                saveStoragePreference();
                ThemeManager.applyTheme(this, findViewById(R.id.fileWriteLayout));
                Toast.makeText(this, getString(R.string.file_load_fb), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.file_not_found_excp), Toast.LENGTH_SHORT).show();
            }
        } else {
            JSONObject targetSettings = FileManager.fetchThemeSettingsFromFile(this, destinationDirectory, fileName);
            if (FileManager.readMeetingsAndSettingsFromFile(this, destinationDirectory, fileName, true)) {
                saveStoragePreference();
                boolean success = FileManager.writeMeetingsAndSettingsToFile(this, destinationDirectory, fileName, MeetingManager.getMeetings(), targetSettings);
                
                if (success) {
                    Toast.makeText(this, getString(R.string.toast_merge_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.io_excp), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.file_not_found_excp), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleDeleteStorageFile() {
        updateDestinationDirectory();

        if (FileManager.deleteFile(destinationDirectory, fileName)) {
            Toast.makeText(this, R.string.toast_file_deleted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.toast_file_delete_error, Toast.LENGTH_SHORT).show();
        }
    }
}
