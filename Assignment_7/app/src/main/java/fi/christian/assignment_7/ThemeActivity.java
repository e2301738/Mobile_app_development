package fi.christian.assignment_7;

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
    private Button saveThemeButton;
    private Button resetThemeButton;
    private ImageButton backButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        initViews();
        setupSpinner();
        
        sharedPreferences = getSharedPreferences(ThemeManager.PREFERENCE_NAME, MODE_PRIVATE);

        loadSettings();
        setupListeners();
    }

    private void setupListeners() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePreview();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        fontSizeSeekBar.setOnSeekBarChangeListener(listener);
        fontColorRedSeekBar.setOnSeekBarChangeListener(listener);
        fontColorGreenSeekBar.setOnSeekBarChangeListener(listener);
        fontColorBlueSeekBar.setOnSeekBarChangeListener(listener);
        backgroundColorRedSeekBar.setOnSeekBarChangeListener(listener);
        backgroundColorGreenSeekBar.setOnSeekBarChangeListener(listener);
        backgroundColorBlueSeekBar.setOnSeekBarChangeListener(listener);

        fontTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePreview();
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

    private void initViews() {
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
        fontSizeSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_FONT_SIZE, ThemeManager.DEFAULT_FONT_SIZE));
        fontTypeSpinner.setSelection(sharedPreferences.getInt(ThemeManager.KEY_FONT_TYPE, ThemeManager.DEFAULT_FONT_TYPE_INDEX));
        
        fontColorRedSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_FONT_COLOR_RED, ThemeManager.DEFAULT_COLOR_BLACK));
        fontColorGreenSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_FONT_COLOR_GREEN, ThemeManager.DEFAULT_COLOR_BLACK));
        fontColorBlueSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_FONT_COLOR_BLUE, ThemeManager.DEFAULT_COLOR_BLACK));
        
        backgroundColorRedSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_BACKGROUND_COLOR_RED, ThemeManager.DEFAULT_COLOR_WHITE));
        backgroundColorGreenSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_BACKGROUND_COLOR_GREEN, ThemeManager.DEFAULT_COLOR_WHITE));
        backgroundColorBlueSeekBar.setProgress(sharedPreferences.getInt(ThemeManager.KEY_BACKGROUND_COLOR_BLUE, ThemeManager.DEFAULT_COLOR_WHITE));

        updatePreview();
    }

    private void resetToDefaults() {
        fontSizeSeekBar.setProgress(ThemeManager.DEFAULT_FONT_SIZE);
        fontTypeSpinner.setSelection(ThemeManager.DEFAULT_FONT_TYPE_INDEX);
        
        fontColorRedSeekBar.setProgress(ThemeManager.DEFAULT_COLOR_BLACK);
        fontColorGreenSeekBar.setProgress(ThemeManager.DEFAULT_COLOR_BLACK);
        fontColorBlueSeekBar.setProgress(ThemeManager.DEFAULT_COLOR_BLACK);
        
        backgroundColorRedSeekBar.setProgress(ThemeManager.DEFAULT_COLOR_WHITE);
        backgroundColorGreenSeekBar.setProgress(ThemeManager.DEFAULT_COLOR_WHITE);
        backgroundColorBlueSeekBar.setProgress(ThemeManager.DEFAULT_COLOR_WHITE);
        
        updatePreview();
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ThemeManager.KEY_FONT_SIZE, fontSizeSeekBar.getProgress());
        editor.putInt(ThemeManager.KEY_FONT_TYPE, fontTypeSpinner.getSelectedItemPosition());
        
        editor.putInt(ThemeManager.KEY_FONT_COLOR_RED, fontColorRedSeekBar.getProgress());
        editor.putInt(ThemeManager.KEY_FONT_COLOR_GREEN, fontColorGreenSeekBar.getProgress());
        editor.putInt(ThemeManager.KEY_FONT_COLOR_BLUE, fontColorBlueSeekBar.getProgress());
        
        editor.putInt(ThemeManager.KEY_BACKGROUND_COLOR_RED, backgroundColorRedSeekBar.getProgress());
        editor.putInt(ThemeManager.KEY_BACKGROUND_COLOR_GREEN, backgroundColorGreenSeekBar.getProgress());
        editor.putInt(ThemeManager.KEY_BACKGROUND_COLOR_BLUE, backgroundColorBlueSeekBar.getProgress());
        
        editor.apply();

        ThemeManager.applyTheme(this, findViewById(R.id.themeLayout));
        Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
    }

    private void updatePreview() {
        int fontSize = fontSizeSeekBar.getProgress();
        int fontColor = Color.rgb(fontColorRedSeekBar.getProgress(), fontColorGreenSeekBar.getProgress(), fontColorBlueSeekBar.getProgress());
        int bgColor = Color.rgb(backgroundColorRedSeekBar.getProgress(), backgroundColorGreenSeekBar.getProgress(), backgroundColorBlueSeekBar.getProgress());
        
        themePreviewTextView.setTextSize(fontSize);
        themePreviewTextView.setTextColor(fontColor);
        themePreviewTextView.setBackgroundColor(bgColor);
        
        Typeface typeface = ThemeManager.getTypeface(fontTypeSpinner.getSelectedItemPosition());
        themePreviewTextView.setTypeface(typeface);
    }
}
