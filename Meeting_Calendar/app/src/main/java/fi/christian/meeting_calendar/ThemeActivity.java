package fi.christian.meeting_calendar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeActivity extends AppCompatActivity {

    private SeekBar fontSizeSeekBar;
    private TextView themePreviewTextView;
    private Spinner fontTypeSpinner;
    private SeekBar fontColorRedSeekBar, fontColorGreenSeekBar, fontColorBlueSeekBar;
    private SeekBar backgroundColorRedSeekBar, backgroundColorGreenSeekBar, backgroundColorBlueSeekBar;
    private Button saveThemeButton, resetThemeButton;
    private ImageButton backButton;
    private SharedPreferences sharedPreferences;

    private String preferenceName;
    private String keyFontSize, keyFontType, keyFontColor, keyBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        initializeStrings();
        initializeViews();
        setupSpinner();
        
        sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE);

        loadSettings();
        setupListeners();
    }

    private void initializeStrings() {
        preferenceName = getString(R.string.prefs_file_name);
        keyFontSize = getString(R.string.json_key_font_size);
        keyFontType = getString(R.string.json_key_font_type);
        keyFontColor = getString(R.string.json_key_font_color);
        keyBackgroundColor = getString(R.string.json_key_background_color);
    }

    private void setupListeners() {
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePreview();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        fontSizeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        fontColorRedSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        fontColorGreenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        fontColorBlueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        backgroundColorRedSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        backgroundColorGreenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        backgroundColorBlueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        fontTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePreview();
                int currentFontColor = Color.rgb(fontColorRedSeekBar.getProgress(), fontColorGreenSeekBar.getProgress(), fontColorBlueSeekBar.getProgress());
                ThemeManager.applyStyleToView(fontTypeSpinner, fontSizeSeekBar.getProgress(), currentFontColor, ThemeManager.getTypeface(fontTypeSpinner.getSelectedItemPosition()));
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        saveThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        resetThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetToDefaults();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.themeLayout));
    }

    private void initializeViews() {
        fontSizeSeekBar = findViewById(R.id.fontSizeSeekBar);
        themePreviewTextView = findViewById(R.id.themePreviewTextView);
        fontTypeSpinner = findViewById(R.id.fontTypeSpinner);
        
        fontColorRedSeekBar = findViewById(R.id.fontColorRedSeekBar);
        fontColorGreenSeekBar = findViewById(R.id.fontColorGreenSeekBar);
        fontColorBlueSeekBar = findViewById(R.id.fontColorBlueSeekBar);
        
        backgroundColorRedSeekBar = findViewById(R.id.backgroundColorRedSeekBar);
        backgroundColorGreenSeekBar = findViewById(R.id.backgroundColorGreenSeekBar);
        backgroundColorBlueSeekBar = findViewById(R.id.backgroundColorBlueSeekBar);
        
        saveThemeButton = findViewById(R.id.saveThemeButton);
        resetThemeButton = findViewById(R.id.resetThemeButton);
        backButton = findViewById(R.id.backButton);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontTypeSpinner.setAdapter(adapter);
    }

    private void loadSettings() {
        fontSizeSeekBar.setProgress(sharedPreferences.getInt(keyFontSize, ThemeManager.DEFAULT_FONT_SIZE));
        fontTypeSpinner.setSelection(sharedPreferences.getInt(keyFontType, ThemeManager.DEFAULT_FONT_TYPE_INDEX));
        
        int fontColor = sharedPreferences.getInt(keyFontColor, ThemeManager.DEFAULT_FONT_COLOR);
        fontColorRedSeekBar.setProgress(Color.red(fontColor));
        fontColorGreenSeekBar.setProgress(Color.green(fontColor));
        fontColorBlueSeekBar.setProgress(Color.blue(fontColor));
        
        int backgroundColor = sharedPreferences.getInt(keyBackgroundColor, ThemeManager.DEFAULT_BACKGROUND_COLOR);
        backgroundColorRedSeekBar.setProgress(Color.red(backgroundColor));
        backgroundColorGreenSeekBar.setProgress(Color.green(backgroundColor));
        backgroundColorBlueSeekBar.setProgress(Color.blue(backgroundColor));

        updatePreview();
    }

    private void resetToDefaults() {
        fontSizeSeekBar.setProgress(ThemeManager.DEFAULT_FONT_SIZE);
        fontTypeSpinner.setSelection(ThemeManager.DEFAULT_FONT_TYPE_INDEX);
        
        fontColorRedSeekBar.setProgress(Color.red(ThemeManager.DEFAULT_FONT_COLOR));
        fontColorGreenSeekBar.setProgress(Color.green(ThemeManager.DEFAULT_FONT_COLOR));
        fontColorBlueSeekBar.setProgress(Color.blue(ThemeManager.DEFAULT_FONT_COLOR));
        
        backgroundColorRedSeekBar.setProgress(Color.red(ThemeManager.DEFAULT_BACKGROUND_COLOR));
        backgroundColorGreenSeekBar.setProgress(Color.green(ThemeManager.DEFAULT_BACKGROUND_COLOR));
        backgroundColorBlueSeekBar.setProgress(Color.blue(ThemeManager.DEFAULT_BACKGROUND_COLOR));
        
        updatePreview();
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(keyFontSize, fontSizeSeekBar.getProgress());
        editor.putInt(keyFontType, fontTypeSpinner.getSelectedItemPosition());
        
        int fontColor = Color.rgb(fontColorRedSeekBar.getProgress(), fontColorGreenSeekBar.getProgress(), fontColorBlueSeekBar.getProgress());
        editor.putInt(keyFontColor, fontColor);
        
        int backgroundColor = Color.rgb(backgroundColorRedSeekBar.getProgress(), backgroundColorGreenSeekBar.getProgress(), backgroundColorBlueSeekBar.getProgress());
        editor.putInt(keyBackgroundColor, backgroundColor);
        
        editor.apply();

        ThemeManager.applyTheme(this, findViewById(R.id.themeLayout));
        Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
    }

    private void updatePreview() {
        int fontSize = fontSizeSeekBar.getProgress();
        int fontColor = Color.rgb(fontColorRedSeekBar.getProgress(), fontColorGreenSeekBar.getProgress(), fontColorBlueSeekBar.getProgress());
        int backgroundColor = Color.rgb(backgroundColorRedSeekBar.getProgress(), backgroundColorGreenSeekBar.getProgress(), backgroundColorBlueSeekBar.getProgress());
        Typeface typeface = ThemeManager.getTypeface(fontTypeSpinner.getSelectedItemPosition());

        themePreviewTextView.setTextSize(fontSize);
        themePreviewTextView.setTextColor(fontColor);
        themePreviewTextView.setBackgroundColor(backgroundColor);
        themePreviewTextView.setTypeface(typeface);
    }
}
