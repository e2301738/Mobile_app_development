package fi.christian.meeting_calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

public class ThemeManager {
    private static final String TAG = "ThemeManager";
    public static final int DEFAULT_FONT_SIZE = 18;
    public static final int DEFAULT_FONT_TYPE_INDEX = 0;
    public static final int DEFAULT_FONT_COLOR = Color.BLACK;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    public static void applyTheme(Context context, View view) {
        String preferenceName = context.getString(R.string.prefs_file_name);
        String keyFontSize = context.getString(R.string.json_key_font_size);
        String keyFontType = context.getString(R.string.json_key_font_type);

        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        int fontSize = sharedPreferences.getInt(keyFontSize, DEFAULT_FONT_SIZE);
        int fontTypePos = sharedPreferences.getInt(keyFontType, DEFAULT_FONT_TYPE_INDEX);
        int fontColor = getFontColor(context);
        int backgroundColor = getBackgroundColor(context);
        Typeface typeface = getTypeface(fontTypePos);

        view.setBackgroundColor(backgroundColor);
        applyStyleToView(view, fontSize, fontColor, typeface);
        loopAllViewObjects(view, fontSize, fontColor, typeface);
    }

    public static int getFontColor(Context context) {
        String preferenceName = context.getString(R.string.prefs_file_name);
        String keyFontColor = context.getString(R.string.json_key_font_color);
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(keyFontColor, DEFAULT_FONT_COLOR);
    }

    public static int getBackgroundColor(Context context) {
        String preferenceName = context.getString(R.string.prefs_file_name);
        String keyBackgroundColor = context.getString(R.string.json_key_background_color);
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(keyBackgroundColor, DEFAULT_BACKGROUND_COLOR);
    }

    public static Typeface getTypeface(int position) {
        switch (position) {
            case 1: return Typeface.DEFAULT_BOLD;
            case 2: return Typeface.SERIF;
            case 3: return Typeface.MONOSPACE;
            default: return Typeface.DEFAULT;
        }
    }

    public static JSONObject getSettingsAsJson(Context context) {
        String preferenceName = context.getString(R.string.prefs_file_name);
        String keyFontSize = context.getString(R.string.json_key_font_size);
        String keyFontType = context.getString(R.string.json_key_font_type);
        String keyFontColor = context.getString(R.string.json_key_font_color);
        String keyBackgroundColor = context.getString(R.string.json_key_background_color);

        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        try {
            JSONObject settingsJsonObject = new JSONObject();
            settingsJsonObject.put(keyFontSize, sharedPreferences.getInt(keyFontSize, DEFAULT_FONT_SIZE));
            settingsJsonObject.put(keyFontType, sharedPreferences.getInt(keyFontType, DEFAULT_FONT_TYPE_INDEX));
            settingsJsonObject.put(keyFontColor, sharedPreferences.getInt(keyFontColor, DEFAULT_FONT_COLOR));
            settingsJsonObject.put(keyBackgroundColor, sharedPreferences.getInt(keyBackgroundColor, DEFAULT_BACKGROUND_COLOR));
            return settingsJsonObject;
        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.log_error_create_json), e);
            return null;
        }
    }

    public static void applySettingsFromJson(Context context, JSONObject settingsJsonObject) {
        String preferenceName = context.getString(R.string.prefs_file_name);
        String keyFontSize = context.getString(R.string.json_key_font_size);
        String keyFontType = context.getString(R.string.json_key_font_type);
        String keyFontColor = context.getString(R.string.json_key_font_color);
        String keyBackgroundColor = context.getString(R.string.json_key_background_color);

        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit();
        try {
            if (settingsJsonObject.has(keyFontSize)) {
                editor.putInt(keyFontSize, settingsJsonObject.getInt(keyFontSize));
            }
            if (settingsJsonObject.has(keyFontType)) {
                editor.putInt(keyFontType, settingsJsonObject.getInt(keyFontType));
            }
            if (settingsJsonObject.has(keyFontColor)) {
                editor.putInt(keyFontColor, settingsJsonObject.getInt(keyFontColor));
            }
            if (settingsJsonObject.has(keyBackgroundColor)) {
                editor.putInt(keyBackgroundColor, settingsJsonObject.getInt(keyBackgroundColor));
            }
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.log_error_apply_json), e);
        }
    }

    public static void loopAllViewObjects(View view, int fontSize, int fontColor, Typeface typeface) {
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
            textView.setTextSize(fontSize);
            textView.setTextColor(fontColor);
            textView.setTypeface(typeface);
        }

        if (view instanceof MaterialButton) {
            MaterialButton button = (MaterialButton) view;
            button.setStrokeColor(ColorStateList.valueOf(fontColor));
            button.setTextColor(fontColor);
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
                textView.setTextSize(fontSize);
                textView.setTypeface(typeface);
            }
        }
    }
}
