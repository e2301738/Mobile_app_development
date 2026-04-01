package fi.christian.assignment_7;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputHandler {

    public static boolean validateInputIsEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setBackgroundColor(Color.RED);
            editText.requestFocus();
            return false;
        }
        return true;
    }
    
    public static boolean validatePickedDateAndTime(Button button, String hint) {
        if (button.getText().toString().equals(hint)) {
            button.setTextColor(Color.RED);
            return false;
        } else {
            button.setTextColor(Color.BLACK);
            return true;
        }
    }

    public static boolean validateParticipants(TextView display, Button button) {
        if (display.getText().toString().equals("No participants selected")) {
            button.setTextColor(Color.RED);
            return false;
        } else {
            button.setTextColor(Color.BLACK);
            return true;
        }
    }

    public static void setupTextWatcher(final EditText editText, final Drawable originalBackground) {
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
