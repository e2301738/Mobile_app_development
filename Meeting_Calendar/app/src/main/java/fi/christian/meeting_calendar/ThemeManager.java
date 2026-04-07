package fi.christian.meeting_calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class ThemeManager {
    public static final String PREFERENCE_NAME = "theme_settings_preferences";
    public static final String KEY_FONT_SIZE = "font_size";
    public static final String KEY_FONT_TYPE = "font_type";
    public static final String KEY_FONT_COLOR = "font_color";
    public static final String KEY_BACKGROUND_COLOR = "background_color";
    public static final int DEFAULT_FONT_SIZE = 18;
    public static final int DEFAULT_FONT_TYPE_INDEX = 0;
    public static final int DEFAULT_FONT_COLOR = Color.BLACK;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    public static void applyTheme(Context context, View view) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        int fontSize = sharedPreferences.getInt(KEY_FONT_SIZE, DEFAULT_FONT_SIZE);
        int fontTypePos = sharedPreferences.getInt(KEY_FONT_TYPE, DEFAULT_FONT_TYPE_INDEX);
        int fontColor = getFontColor(context);
        int backgroundColor = getBackgroundColor(context);
        Typeface typeface = getTypeface(fontTypePos);

        view.setBackgroundColor(backgroundColor);
        applyStyleToView(view, fontSize, fontColor, typeface);
        loopAllViewObjects(view, fontSize, fontColor, typeface);
    }

    public static int getFontColor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_FONT_COLOR, DEFAULT_FONT_COLOR);
    }

    public static int getBackgroundColor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_BACKGROUND_COLOR, DEFAULT_BACKGROUND_COLOR);
    }

    public static Typeface getTypeface(int position) {
        switch (position) {
            case 1: return Typeface.DEFAULT_BOLD;
            case 2: return Typeface.SERIF;
            case 3: return Typeface.MONOSPACE;
            default: return Typeface.DEFAULT;
        }
    }

    private static void loopAllViewObjects(View view, int fontSize, int fontColor, Typeface typeface) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                applyStyleToView(child, fontSize, fontColor, typeface);
                loopAllViewObjects(child, fontSize, fontColor, typeface);
            }
        }
    }

    public static void applyStyleToView(View view, int fontSize, int fontColor, Typeface typeface) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            textView.setTextColor(fontColor);
            textView.setTypeface(typeface);
        }

        if (view instanceof MaterialButton) {
            MaterialButton button = (MaterialButton) view;
            button.setStrokeColor(ColorStateList.valueOf(fontColor));
            button.setStrokeWidth(3);
        }

        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setHintTextColor(fontColor & 0x80FFFFFF);
            editText.setTextColor(fontColor);
        }

        if (view instanceof Spinner){
            Spinner spinner = (Spinner) view;
            spinner.setBackgroundTintList(ColorStateList.valueOf(fontColor));
            View selectedView = spinner.getSelectedView();
            if (selectedView instanceof TextView) {
                TextView textView = (TextView) selectedView;
                textView.setTextColor(fontColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                textView.setTypeface(typeface);
            }
        }
    }
}
