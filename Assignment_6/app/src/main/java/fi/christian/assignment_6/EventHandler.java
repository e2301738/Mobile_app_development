package fi.christian.assignment_6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventHandler {
    private static ArrayList<Event> eventList = new ArrayList<>();
    private static ArrayList<String> months = new ArrayList<>();

    public static void setMonths(String[] monthsArray) {
        months.clear();
        Collections.addAll(months, monthsArray);
    }

    public static ArrayList<String> getMonths() {
        return months;
    }

    public static ArrayList<Event> getEventList() {
        if (eventList.isEmpty()) {
            generateMockData();
        }
        return eventList;
    }

    public static void addEvent(Event event) {
        eventList.add(event);
        sortEvents();
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

    public static void sortEvents() {

        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                int month1 = getMonthIndex(event1.getDate());
                int month2 = getMonthIndex(event2.getDate());
                if (month1 != month2) {
                    return Integer.compare(month1, month2);
                }
                
                int day1 = getDay(event1.getDate());
                int day2 = getDay(event2.getDate());
                if (day1 != day2) {
                    return Integer.compare(day1, day2);
                }

                String time1 = event1.getTime();
                String time2 = event2.getTime();
                return time1.compareTo(time2);
            }
            
            private int getMonthIndex(String dateStr) {
                String[] parts = dateStr.split(" ");
                return months.indexOf(parts[1]);
            }
            
            private int getDay(String dateStr) {
                String[] parts = dateStr.split(" ");
                return Integer.parseInt(parts[0]);
            }
        });
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
