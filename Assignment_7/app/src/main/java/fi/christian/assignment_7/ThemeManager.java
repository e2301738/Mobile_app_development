package fi.christian.assignment_7;

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
    public static final String KEY_FONT_COLOR_RED = "font_color_red";
    public static final String KEY_FONT_COLOR_GREEN = "font_color_green";
    public static final String KEY_FONT_COLOR_BLUE = "font_color_blue";
    public static final String KEY_BACKGROUND_COLOR_RED = "background_color_red";
    public static final String KEY_BACKGROUND_COLOR_GREEN = "background_color_green";
    public static final String KEY_BACKGROUND_COLOR_BLUE = "background_color_blue";
    public static final int DEFAULT_FONT_SIZE = 18;
    public static final int DEFAULT_FONT_TYPE_INDEX = 0;
    public static final int DEFAULT_COLOR_BLACK = 0;
    public static final int DEFAULT_COLOR_WHITE = 255;

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
        int r = sharedPreferences.getInt(KEY_FONT_COLOR_RED, DEFAULT_COLOR_BLACK);
        int g = sharedPreferences.getInt(KEY_FONT_COLOR_GREEN, DEFAULT_COLOR_BLACK);
        int b = sharedPreferences.getInt(KEY_FONT_COLOR_BLUE, DEFAULT_COLOR_BLACK);
        return Color.rgb(r, g, b);
    }

    public static int getBackgroundColor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int r = sharedPreferences.getInt(KEY_BACKGROUND_COLOR_RED, DEFAULT_COLOR_WHITE);
        int g = sharedPreferences.getInt(KEY_BACKGROUND_COLOR_GREEN, DEFAULT_COLOR_WHITE);
        int b = sharedPreferences.getInt(KEY_BACKGROUND_COLOR_BLUE, DEFAULT_COLOR_WHITE);
        return Color.rgb(r, g, b);
    }

    public static Typeface getTypeface(int position) {
        switch (position) {
            case 1: return Typeface.MONOSPACE;
            case 2: return Typeface.SERIF;
            case 3: return Typeface.SANS_SERIF;
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

    private static void applyStyleToView(View view, int fontSize, int fontColor, Typeface typeface) {
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
                ((TextView) selectedView).setTextColor(fontColor);
            }
        }
    }
}
