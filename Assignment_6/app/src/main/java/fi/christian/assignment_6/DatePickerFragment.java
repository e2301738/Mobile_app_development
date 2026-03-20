package fi.christian.assignment_6;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        if ("DatePicker".equals(getTag())) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ArrayList<String> months = EventHandler.getMonths();
        String monthName = months.get(month);
        
        String dateValue = dayOfMonth + " " + monthName;

        String message = getString(R.string.selected_date_is, dateValue);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        TextView targetTextView;
        if ("SearchDatePicker".equals(getTag())) {
            targetTextView = getActivity().findViewById(R.id.search_date_TextView);
        } else {
            targetTextView = getActivity().findViewById(R.id.selected_date_TextView);
        }

        targetTextView.setText(dateValue);
    }
}
