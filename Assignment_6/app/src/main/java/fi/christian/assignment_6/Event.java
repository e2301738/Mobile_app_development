package fi.christian.assignment_6;

public class Event {
    private final String eventType;
    private final String date;
    private final String time;

    public Event(String eventType, String date, String time) {
        this.eventType = eventType;
        this.date = date;
        this.time = time;
    }

    public String getType() { return eventType; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(eventType);
        stringBuilder.append(" – ");
        stringBuilder.append(date);
        stringBuilder.append(" – ");
        stringBuilder.append(time);
        return stringBuilder.toString();
    }
}
