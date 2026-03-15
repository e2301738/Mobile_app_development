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
    private TextView selectedDateTextView, selectedTimeTextView;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventTypeSpinner = findViewById(R.id.spinner_event_type);
        selectedDateTextView = findViewById(R.id.selected_date_TextView);
        selectedTimeTextView = findViewById(R.id.selected_time_TextView);
        eventRecyclerView = findViewById(R.id.event_RecyclerView);

        Button setDateButton = findViewById(R.id.button_set_date);
        Button setTimeButton = findViewById(R.id.button_set_time);
        Button submitEventButton = findViewById(R.id.button_submit);

        eventAdapter = new EventAdapter(EventHandler.getEventList());
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(eventAdapter);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
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
    }

    private void showDatePicker() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePicker() {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
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
        
        eventAdapter.notifyDataSetChanged();

        resetForm(noDate, noTime);
        showToast(getString(R.string.toast_event_added));
    }

    private void resetForm(String noDate, String noTime) {
        selectedDateTextView.setText(noDate);
        selectedTimeTextView.setText(noTime);
        eventTypeSpinner.setSelection(0);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
