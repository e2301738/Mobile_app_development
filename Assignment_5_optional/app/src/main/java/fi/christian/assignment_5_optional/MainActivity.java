package fi.christian.assignment_5_optional;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneEditText;
    private AutoCompleteTextView educationInputCombo;
    private CheckBox readingCheckBox, sportsCheckBox, musicCheckBox;
    private AutoCompleteTextView firstNameAutoCompleteTextView, lastNameAutoCompleteTextView, educationAutoCompleteTextView;
    private Button submitButton, refreshButton;
    private ArrayAdapter<String> firstNameAdapter, lastNameAdapter, educationAdapter;
    private Drawable firstNameBackground, lastNameBackground, phoneBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        educationInputCombo = findViewById(R.id.education_auto_complete_input);
        readingCheckBox = findViewById(R.id.reading_check_box);
        sportsCheckBox = findViewById(R.id.sports_check_box);
        musicCheckBox = findViewById(R.id.music_check_box);
        submitButton = findViewById(R.id.submit_button);
        refreshButton = findViewById(R.id.refresh_button);

        firstNameBackground = firstNameEditText.getBackground();
        lastNameBackground = lastNameEditText.getBackground();
        phoneBackground = phoneEditText.getBackground();

        firstNameAutoCompleteTextView = findViewById(R.id.first_name_auto_complete);
        lastNameAutoCompleteTextView = findViewById(R.id.last_name_auto_complete);
        educationAutoCompleteTextView = findViewById(R.id.education_auto_complete);

        firstNameAutoCompleteTextView.setThreshold(1);
        lastNameAutoCompleteTextView.setThreshold(1);
        educationAutoCompleteTextView.setThreshold(1);

        ArrayAdapter<String> comboAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.education_levels));
        educationInputCombo.setAdapter(comboAdapter);

        updateAdapters();

        firstNameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String formatted = CatalogHandler.getFormattedResult(firstNameAutoCompleteTextView.getText().toString(), CatalogHandler.SEARCH_CHOICE_FIRST_NAME, MainActivity.this);
                firstNameAutoCompleteTextView.setText(formatted);
            }
        });

        lastNameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String formatted = CatalogHandler.getFormattedResult(lastNameAutoCompleteTextView.getText().toString(), CatalogHandler.SEARCH_CHOICE_LAST_NAME, MainActivity.this);
                lastNameAutoCompleteTextView.setText(formatted);
            }
        });

        educationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String formatted = CatalogHandler.getFormattedResult(educationAutoCompleteTextView.getText().toString(), CatalogHandler.SEARCH_CHOICE_EDUCATION, MainActivity.this);
                educationAutoCompleteTextView.setText(formatted);
            }
        });

        setupTextWatcher(firstNameEditText, firstNameBackground);
        setupTextWatcher(lastNameEditText, lastNameBackground);
        setupTextWatcher(phoneEditText, phoneBackground);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String education = educationInputCombo.getText().toString().trim();

                ArrayList<String> hobbies = new ArrayList<>();
                if (readingCheckBox.isChecked()) {
                    hobbies.add(readingCheckBox.getText().toString());
                }
                if (sportsCheckBox.isChecked()) {
                    hobbies.add(sportsCheckBox.getText().toString());
                }
                if (musicCheckBox.isChecked()) {
                    hobbies.add(musicCheckBox.getText().toString());
                }

                boolean isEmpty = false;

                if (phone.isEmpty()) {
                    phoneEditText.setBackgroundColor(Color.RED);
                    phoneEditText.requestFocus();
                    isEmpty = true;
                }
                if (lastName.isEmpty()) {
                    lastNameEditText.setBackgroundColor(Color.RED);
                    lastNameEditText.requestFocus();
                    isEmpty = true;
                }
                if (firstName.isEmpty()) {
                    firstNameEditText.setBackgroundColor(Color.RED);
                    firstNameEditText.requestFocus();
                    isEmpty = true;
                }

                if (isEmpty) {
                    showToast(getString(R.string.fill_all_fields_toast));
                    return;
                }
                if (CatalogHandler.isDuplicatePhone(phone)) {
                    phoneEditText.setBackgroundColor(Color.RED);
                    phoneEditText.requestFocus();
                    showToast(getString(R.string.duplicate_phone_toast));
                    return;
                }

                Person newPerson = new Person(firstName, lastName, phone, education, hobbies);
                CatalogHandler.addEntry(newPerson);

                updateAdapters();

                firstNameEditText.setText("");
                lastNameEditText.setText("");
                phoneEditText.setText("");
                educationInputCombo.setText("");
                readingCheckBox.setChecked(false);
                sportsCheckBox.setChecked(false);
                musicCheckBox.setChecked(false);

                showToast(getString(R.string.data_submitted_toast));
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameAutoCompleteTextView.setText("");
                lastNameAutoCompleteTextView.setText("");
                educationAutoCompleteTextView.setText("");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateAdapters() {
        firstNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CatalogHandler.firstNameList);
        firstNameAutoCompleteTextView.setAdapter(firstNameAdapter);

        lastNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CatalogHandler.lastNameList);
        lastNameAutoCompleteTextView.setAdapter(lastNameAdapter);

        educationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CatalogHandler.educationList);
        educationAutoCompleteTextView.setAdapter(educationAdapter);
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
}
