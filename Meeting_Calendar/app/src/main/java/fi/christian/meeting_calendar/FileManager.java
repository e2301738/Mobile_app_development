package fi.christian.meeting_calendar;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileManager {
    private static final int BLOCK_SIZE = 128;

    public static boolean saveMeetings(Context context, File directory, String fileName, ArrayList<Meeting> meetings) {
        return saveMeetings(context, directory, fileName, meetings, null);
    }

    public static boolean saveMeetings(Context context, File directory, String fileName, ArrayList<Meeting> meetings, JSONObject settings) {
        try {
            JSONObject rootJsonObject = new JSONObject();
            JSONArray meetingsJsonArray = new JSONArray();

            for (Meeting meeting : meetings) {
                JSONObject meetingJsonObject = new JSONObject();
                meetingJsonObject.put(context.getString(R.string.json_key_title), meeting.getTitle());
                meetingJsonObject.put(context.getString(R.string.json_key_place), meeting.getPlace());
                meetingJsonObject.put(context.getString(R.string.json_key_date), meeting.getDate());
                meetingJsonObject.put(context.getString(R.string.json_key_time), meeting.getTime());

                JSONArray participantsJsonArray = new JSONArray();
                for (String participant : meeting.getParticipants()) {
                    participantsJsonArray.put(participant);
                }
                meetingJsonObject.put(context.getString(R.string.json_key_participants), participantsJsonArray);
                meetingsJsonArray.put(meetingJsonObject);
            }
            rootJsonObject.put(context.getString(R.string.json_key_meetings), meetingsJsonArray);
            
            if (settings != null) {
                rootJsonObject.put(context.getString(R.string.json_key_settings), settings);
            } else {
                rootJsonObject.put(context.getString(R.string.json_key_settings), ThemeManager.getSettingsAsJson(context));
            }

            File file = new File(directory, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(rootJsonObject.toString(4));
            outputStreamWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject getSettingsFromFile(Context context, File directory, String fileName) {
        String content = readFileContent(new File(directory, fileName));
        try {
            JSONObject root = new JSONObject(content);
            return root.optJSONObject(context.getString(R.string.json_key_settings));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean loadMeetings(Context context, File directory, String fileName, boolean merge) {
        String content = readFileContent(new File(directory, fileName));
        if (content.isEmpty()) return false;

        try {
            JSONObject rootJsonObject = new JSONObject(content);

            if (!merge) {
                MeetingManager.clearMeetings();
                String keySettings = context.getString(R.string.json_key_settings);
                if (rootJsonObject.has(keySettings)) {
                    ThemeManager.applySettingsFromJson(context, rootJsonObject.getJSONObject(keySettings));
                }
            }

            String keyMeetings = context.getString(R.string.json_key_meetings);
            if (rootJsonObject.has(keyMeetings)) {
                JSONArray meetingsArray = rootJsonObject.getJSONArray(keyMeetings);
                for (int i = 0; i < meetingsArray.length(); i++) {
                    JSONObject jsonObject = meetingsArray.getJSONObject(i);
                    ArrayList<String> participants = new ArrayList<>();
                    JSONArray participantsArray = jsonObject.optJSONArray(context.getString(R.string.json_key_participants));

                    if (participantsArray != null) {
                        for (int j = 0; j < participantsArray.length(); j++) {
                            participants.add(participantsArray.getString(j));
                        }
                    }

                    Meeting meeting = new Meeting(
                            jsonObject.getString(context.getString(R.string.json_key_title)),
                            jsonObject.getString(context.getString(R.string.json_key_place)),
                            participants,
                            jsonObject.getString(context.getString(R.string.json_key_date)),
                            jsonObject.getString(context.getString(R.string.json_key_time))
                    );
                    MeetingManager.addMeeting(meeting);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String readFileContent(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] inputBuffer = new char[BLOCK_SIZE];
            StringBuilder stringBuilder = new StringBuilder();
            int charRead;

            while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
                stringBuilder.append(String.copyValueOf(inputBuffer, 0, charRead));
                inputBuffer = new char[BLOCK_SIZE];
            }
            inputStreamReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
