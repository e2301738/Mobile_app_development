package fi.christian.meeting_calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class FileActivity extends AppCompatActivity {

    private RadioButton internalRadioButton, externalRadioButton;
    private Button saveToFileButton, readFromFileButton;
    private ImageButton backButton;

    private String destinationPath;
    private String fileName;
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
        backButton = findViewById(R.id.backButton);
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
                readMeetingsFromFile();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void readMeetingsFromFile() {
        updateDestinationDirectory();

        if (destinationDirectory == null) {
            Toast.makeText(this, R.string.storage_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = FileManager.loadMeetings(this, destinationDirectory, fileName);

        if (success) {
            Toast.makeText(this, getString(R.string.file_load_fb), Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(destinationDirectory, fileName);
            if (!file.exists()) {
                Toast.makeText(this, getString(R.string.file_not_found_excp), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.io_excp), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
