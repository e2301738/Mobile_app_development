package fi.christian.assignment_7;

public class Meeting {
    private String title;
    private String place;
    private String participants;
    private String date;
    private String time;

    public Meeting(String title, String place, String participants, String date, String time) {
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
    public String getParticipants() {
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
        stringbuilder.append("Participants: ").append(participants).append("\n");
        stringbuilder.append("Date: ").append(date).append("\n");
        stringbuilder.append("Time: ").append(time);
        return stringbuilder.toString();
    }
}
