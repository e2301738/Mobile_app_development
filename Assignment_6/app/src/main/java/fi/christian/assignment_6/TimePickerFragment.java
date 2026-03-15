package fi.christian.assignment_6;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeValue = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        
        Toast.makeText(getActivity(), "Selected time is: " + timeValue, Toast.LENGTH_SHORT).show();

        TextView selectedTimeTextView = getActivity().findViewById(R.id.selected_time_TextView);
        selectedTimeTextView.setText(timeValue);
    }
}
