package fi.christian.assignment_6;

import java.util.ArrayList;

public class EventHandler {
    private static ArrayList<Event> eventList = new ArrayList<>();

    public static ArrayList<Event> getEventList() {
        if (eventList.isEmpty()) {
            generateMockData();
        }
        return eventList;
    }

    public static void addEvent(Event event) {
        eventList.add(event);
    }

    public static ArrayList<Event> getFilteredList(String searchType, String searchDate) {
        ArrayList<Event> filteredList = new ArrayList<>();
        
        for (Event event : eventList) {
            boolean typeMatches = searchType.isEmpty() || event.getType().equals(searchType);
            boolean dateMatches = searchDate.isEmpty() || event.getDate().equals(searchDate);
            
            if (typeMatches && dateMatches) {
                filteredList.add(event);
            }
        }
        return filteredList;
    }

    private static void generateMockData() {
        eventList.add(new Event("Meeting", "15 Mar", "09:00"));
        eventList.add(new Event("Meeting", "15 Mar", "14:30"));
        eventList.add(new Event("Birthday", "16 Mar", "18:00"));
        eventList.add(new Event("Work", "16 Mar", "08:00"));
        eventList.add(new Event("School", "17 Mar", "10:15"));
        eventList.add(new Event("Work", "17 Mar", "16:00"));
        eventList.add(new Event("Birthday", "18 Mar", "12:00"));
        eventList.add(new Event("Meeting", "19 Mar", "11:00"));
        eventList.add(new Event("School", "20 Mar", "08:30"));
        eventList.add(new Event("Work", "20 Mar", "13:00"));
    }
}
