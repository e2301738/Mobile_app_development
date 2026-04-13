package fi.christian.meeting_calendar;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Meeting {
    private long id;
    private final String title;
    private final String place;
    private final ArrayList<Participant> participants;
    private final String date;
    private final String time;

    public Meeting(String title, String place, ArrayList<Participant> participants, String date, String time) {
        this.title = title;
        this.place = place;
        this.participants = participants;
        this.date = date;
        this.time = time;
    }

    public Meeting(long id, String title, String place, ArrayList<Participant> participants, String date, String time) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.participants = participants;
        this.date = date;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public String getPlace() {
        return place;
    }
    public ArrayList<Participant> getParticipants() {
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
        
        String participantsNames = participants.stream()
                .map(Participant::getName)
                .collect(Collectors.joining(", "));
                
        stringBuilder.append("Participants: ").append(participantsNames).append("\n");
        stringBuilder.append("Date: ").append(date).append("\n");
        stringBuilder.append("Time: ").append(time).append("\n");
        return stringBuilder.toString();
    }
}
