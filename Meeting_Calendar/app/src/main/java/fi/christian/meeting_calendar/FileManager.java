package fi.christian.meeting_calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileManager {
    private static final int BLOCK_SIZE = 128;

    public static String readFromAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
        return stringBuilder.toString();
    }

    public static boolean writeMeetingsAndSettingsToFile(Context context, File directory, String fileName, ArrayList<Meeting> meetings, JSONObject settings) {
        try {
            JSONObject rootJSON = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (Meeting meeting : meetings) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(context.getString(R.string.json_key_title), meeting.getTitle());
                jsonObject.put(context.getString(R.string.json_key_place), meeting.getPlace());
                jsonObject.put(context.getString(R.string.json_key_date), meeting.getDate());
                jsonObject.put(context.getString(R.string.json_key_time), meeting.getTime());
                
                JSONArray participantsArray = new JSONArray();
                for (Participant p : meeting.getParticipants()) {
                    JSONObject pJson = new JSONObject();
                    pJson.put("name", p.getName());
                    
                    if (p.getImage() != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        p.getImage().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        pJson.put("image", encodedImage);
                    }
                    
                    participantsArray.put(pJson);
                }
                jsonObject.put(context.getString(R.string.json_key_participants), participantsArray);
                
                jsonArray.put(jsonObject);
            }
            rootJSON.put(context.getString(R.string.json_key_meetings), jsonArray);
            rootJSON.put(context.getString(R.string.json_key_settings), settings != null ? settings : ThemeManager.getSettingsAsJson(context));

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(directory, fileName)));
            outputStreamWriter.write(rootJSON.toString(4));
            outputStreamWriter.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static JSONObject fetchThemeSettingsFromFile(Context context, File directory, String fileName) {
        try {
            JSONObject rootJSON = new JSONObject(readFileContent(new File(directory, fileName)));
            return rootJSON.getJSONObject(context.getString(R.string.json_key_settings));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean readMeetingsAndSettingsFromFile(Context context, File directory, String fileName, boolean merge) {
        try {
            JSONObject rootJSON = new JSONObject(readFileContent(new File(directory, fileName)));

            if (!merge) {
                MeetingManager.clearMeetings();
                ThemeManager.applySettingsFromJson(context, rootJSON.getJSONObject(context.getString(R.string.json_key_settings)));
            }

            JSONArray array = rootJSON.getJSONArray(context.getString(R.string.json_key_meetings));
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                JSONArray participantsArray = jsonObject.getJSONArray(context.getString(R.string.json_key_participants));
                ArrayList<Participant> participants = new ArrayList<>();
                for (int j = 0; j < participantsArray.length(); j++) {
                    JSONObject participantsJsonObject = participantsArray.getJSONObject(j);
                    String name = participantsJsonObject.getString("name");
                    
                    Bitmap image = null;
                    if (participantsJsonObject.has("image")) {
                        String encodedImage = participantsJsonObject.getString("image");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    }
                    
                    participants.add(new Participant(name, image));
                }

                MeetingManager.addMeeting(new Meeting(
                        jsonObject.getString(context.getString(R.string.json_key_title)),
                        jsonObject.getString(context.getString(R.string.json_key_place)),
                        participants,
                        jsonObject.getString(context.getString(R.string.json_key_date)),
                        jsonObject.getString(context.getString(R.string.json_key_time))
                ));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteFile(File directory, String fileName) {
        try {
            File file = new File(directory, fileName);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static String readFileContent(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] buffer = new char[BLOCK_SIZE];
            StringBuilder stringBuilder = new StringBuilder();
            int read;
            while ((read = inputStreamReader.read(buffer)) > 0) {
                stringBuilder.append(String.copyValueOf(buffer, 0, read));
            }
            inputStreamReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
