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

    public static ArrayList<Event> getFilteredList(String searchType, String searchDate) {
        ArrayList<Event> filteredList = new ArrayList<>();
        
        for (Event event : eventList) {
            boolean eventMatches = event.getType().equals(searchType);
            boolean dateMatches = (searchDate == null) || event.getDate().equals(searchDate);
            
            if (eventMatches && dateMatches) {
                filteredList.add(event);
            }
        }
        return filteredList;
    }
}
