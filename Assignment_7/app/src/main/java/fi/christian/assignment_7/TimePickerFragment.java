package fi.christian.assignment_7;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(requireActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeValue = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        
        Button targetButton;
        if ("UpdateTimePicker".equals(getTag())) {
            targetButton = getActivity().findViewById(R.id.timeButton);
        } else {
            targetButton = getActivity().findViewById(R.id.timeButton);
        }
        targetButton.setTextColor(Color.BLACK);
        targetButton.setText(timeValue);
    }
}
