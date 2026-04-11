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

    public static boolean loadMeetings(Context context, File directory, String fileName) {
        File file = new File(directory, fileName);
        if (!file.exists()) return false;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] inputBuffer = new char[BLOCK_SIZE];
            StringBuilder fileContent = new StringBuilder();
            int charRead;

            while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
                fileContent.append(String.copyValueOf(inputBuffer, 0, charRead));
            }
            inputStreamReader.close();

            JSONObject rootJsonObject = new JSONObject(fileContent.toString());
            MeetingManager.clearMeetings();

            String keyMeetings = context.getString(R.string.json_key_meetings);
            if (rootJsonObject.has(keyMeetings)) {
                JSONArray meetingsArray = rootJsonObject.getJSONArray(keyMeetings);
                for (int i = 0; i < meetingsArray.length(); i++) {
                    JSONObject obj = meetingsArray.getJSONObject(i);
                    ArrayList<String> participants = new ArrayList<>();
                    JSONArray pArray = obj.getJSONArray(context.getString(R.string.json_key_participants));
                    for (int j = 0; j < pArray.length(); j++) {
                        participants.add(pArray.getString(j));
                    }

                    Meeting meeting = new Meeting(
                            obj.getString(context.getString(R.string.json_key_title)),
                            obj.getString(context.getString(R.string.json_key_place)),
                            participants,
                            obj.getString(context.getString(R.string.json_key_date)),
                            obj.getString(context.getString(R.string.json_key_time))
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
}
