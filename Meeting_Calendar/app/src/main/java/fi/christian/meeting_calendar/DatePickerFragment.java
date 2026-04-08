package fi.christian.meeting_calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateValue = String.format(Locale.getDefault(), "%02d.%02d.%d", dayOfMonth, month + 1, year);

        Button targetButton;
        if ("DatePicker".equals(getTag()) || "UpdateDatePicker".equals(getTag())) {
            targetButton = getActivity().findViewById(R.id.dateButton);
        } else {
            targetButton = getActivity().findViewById(R.id.searchDateButton);
        }
        targetButton.setText(dateValue);
        targetButton.setTextColor(ThemeManager.getFontColor(getActivity()));
    }
}
