package fi.christian.assignment_6;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String[] monthAbbreviations = getResources().getStringArray(R.array.month_abbreviations);
        String dateValue = dayOfMonth + " " + monthAbbreviations[month];

        String message = getString(R.string.selected_date_is, dateValue);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        TextView selectedDateTextView = getActivity().findViewById(R.id.selected_date_TextView);
        selectedDateTextView.setText(dateValue);
    }
}
