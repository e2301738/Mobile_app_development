package fi.christian.assignment_5;

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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneEditText;
    private AutoCompleteTextView firstNameAutoCompleteTextView, lastNameAutoCompleteTextView, phoneAutoCompleteTextView;
    private Button submitButton;
    private ArrayAdapter<String> firstNameAdapter, lastNameAdapter, phoneAdapter;
    private Drawable firstNameBackground, lastNameBackground, phoneBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        submitButton = findViewById(R.id.submit_button);

        firstNameBackground = firstNameEditText.getBackground();
        lastNameBackground = lastNameEditText.getBackground();
        phoneBackground = phoneEditText.getBackground();

        firstNameAutoCompleteTextView = findViewById(R.id.first_name_auto_complete);
        lastNameAutoCompleteTextView = findViewById(R.id.last_name_auto_complete);
        phoneAutoCompleteTextView = findViewById(R.id.phone_auto_complete);

        firstNameAutoCompleteTextView.setThreshold(1);
        lastNameAutoCompleteTextView.setThreshold(1);
        phoneAutoCompleteTextView.setThreshold(1);

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

        phoneAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String formatted = CatalogHandler.getFormattedResult(phoneAutoCompleteTextView.getText().toString(), CatalogHandler.SEARCH_CHOICE_PHONE, MainActivity.this);
                phoneAutoCompleteTextView.setText(formatted);
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
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.fill_all_fields_toast), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (CatalogHandler.isDuplicatePhone(phone)) {
                    phoneEditText.setBackgroundColor(Color.RED);
                    phoneEditText.requestFocus();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.duplicate_phone_toast), Toast.LENGTH_SHORT).show();
                    return;
                }

                Person newPerson = new Person(firstName, lastName, phone);
                CatalogHandler.addEntry(newPerson);

                updateAdapters();

                firstNameEditText.setText("");
                lastNameEditText.setText("");
                phoneEditText.setText("");

                Toast.makeText(MainActivity.this, getResources().getString(R.string.data_submitted_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdapters() {
        firstNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CatalogHandler.firstNameList);
        firstNameAutoCompleteTextView.setAdapter(firstNameAdapter);

        lastNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CatalogHandler.lastNameList);
        lastNameAutoCompleteTextView.setAdapter(lastNameAdapter);

        phoneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, CatalogHandler.phoneList);
        phoneAutoCompleteTextView.setAdapter(phoneAdapter);
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
