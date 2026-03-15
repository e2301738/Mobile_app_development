package fi.christian.assignment_6;

import java.util.ArrayList;

public class EventHandler {
    private static ArrayList<Event> eventList = new ArrayList<>();

    public static ArrayList<Event> getEventList() {
        return eventList;
    }

    public static void addEvent(Event event) {
        eventList.add(event);
    }
}
