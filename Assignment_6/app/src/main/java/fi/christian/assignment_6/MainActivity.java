package fi.christian.assignment_6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private Spinner eventTypeSpinner;
    private Spinner searchTypeSpinner;
    private TextView selectedDateTextView, selectedTimeTextView, searchDateTextView;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private Button setDateButton, setTimeButton, submitEventButton, searchButton, clearSearchButton, searchDateButton;
    private static final String NO_FILTER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventTypeSpinner = findViewById(R.id.spinner_event_type);
        searchTypeSpinner = findViewById(R.id.spinner_search_type);
        selectedDateTextView = findViewById(R.id.selected_date_TextView);
        selectedTimeTextView = findViewById(R.id.selected_time_TextView);
        searchDateTextView = findViewById(R.id.search_date_TextView);
        eventRecyclerView = findViewById(R.id.event_RecyclerView);

        setDateButton = findViewById(R.id.button_set_date);
        setTimeButton = findViewById(R.id.button_set_time);
        submitEventButton = findViewById(R.id.button_submit);
        searchButton = findViewById(R.id.button_search);
        clearSearchButton = findViewById(R.id.button_clear_search);
        searchDateButton = findViewById(R.id.button_set_search_date);

        eventAdapter = new EventAdapter(EventHandler.getEventList());
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(eventAdapter);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("DatePicker");
            }
        });

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        submitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEvents();
            }
        });

        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearch();
            }
        });

        searchDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("SearchDatePicker");
            }
        });
    }

    private void searchEvents() {
        String selectedType = searchTypeSpinner.getSelectedItem().toString();
        String eventTypeParameter;
        if (selectedType.equals(getString(R.string.all_events))) {
            eventTypeParameter = NO_FILTER;
        } else {
            eventTypeParameter = selectedType;
        }

        String selectedDate = searchDateTextView.getText().toString();
        String dateParameter;
        if (selectedDate.equals(getString(R.string.no_search_date))) {
            dateParameter = NO_FILTER;
        } else {
            dateParameter = selectedDate;
        }
        
        eventAdapter.updateList(EventHandler.getFilteredList(eventTypeParameter, dateParameter));
    }

    private void clearSearch() {
        searchDateTextView.setText(R.string.no_search_date);
        searchTypeSpinner.setSelection(0);
        eventAdapter.updateList(EventHandler.getEventList());
    }

    private void showDatePicker(String tag) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), tag);
    }

    private void showTimePicker() {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "TimePicker");
    }

    private void addEvent() {
        String dateString = selectedDateTextView.getText().toString();
        String timeString = selectedTimeTextView.getText().toString();
        
        String noDate = getString(R.string.no_date_selected);
        String noTime = getString(R.string.no_time_selected);

        if (dateString.equals(noDate) || timeString.equals(noTime)) {
            showToast(getString(R.string.toast_select_date_time));
            return;
        }

        String eventType = eventTypeSpinner.getSelectedItem().toString();

        Event newEvent = new Event(eventType, dateString, timeString);
        EventHandler.addEvent(newEvent);
        
        eventAdapter.updateList(EventHandler.getEventList());

        resetForm(noDate, noTime);
        showToast(getString(R.string.toast_event_added));
    }

    private void resetForm(String noDate, String noTime) {
        selectedDateTextView.setText(noDate);
        selectedTimeTextView.setText(noTime);
        eventTypeSpinner.setSelection(0);
        clearSearch();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
