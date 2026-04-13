package fi.christian.meeting_calendar;

import android.text.TextUtils;
import java.util.ArrayList;

public class Meeting {
    private final String title;
    private final String place;
    private final ArrayList<String> participants;
    private final String date;
    private final String time;

    public Meeting(String title, String place, ArrayList<String> participants, String date, String time) {
        this.title = title;
        this.place = place;
        this.participants = participants;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }
    public String getPlace() {
        return place;
    }
    public ArrayList<String> getParticipants() {
        return participants;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(title).append("\n");
        stringBuilder.append("Place: ").append(place).append("\n");
        stringBuilder.append("Participants: ").append(TextUtils.join(", ", participants)).append("\n");
        stringBuilder.append("Date: ").append(date).append("\n");
        stringBuilder.append("Time: ").append(time).append("\n");
        return stringBuilder.toString();
    }
}
