package fi.christian.meeting_calendar;

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
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("Title: ").append(title).append("\n");
        stringbuilder.append("Place: ").append(place).append("\n");
        stringbuilder.append("Participants: ").append(String.join(", ", participants)).append("\n");
        stringbuilder.append("Date: ").append(date).append("\n");
        stringbuilder.append("Time: ").append(time).append("\n");
        return stringbuilder.toString();
    }
}
