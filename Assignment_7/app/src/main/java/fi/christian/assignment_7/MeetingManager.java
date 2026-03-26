package fi.christian.assignment_7;

import java.util.ArrayList;
import java.util.Comparator;

public class MeetingManager {
    private static final ArrayList<Meeting> meetings = new ArrayList<>();

    public static void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    public static ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public static ArrayList<Meeting> searchMeetings(String searchString) {
        ArrayList<Meeting> results = new ArrayList<>();
        String lowerCaseSearchString = searchString.toLowerCase();

        for (Meeting meeting : meetings) {
            if (meeting.getTitle().toLowerCase().contains(lowerCaseSearchString) || 
                meeting.getPlace().toLowerCase().contains(lowerCaseSearchString) ||
                meeting.getDate().toLowerCase().contains(lowerCaseSearchString) ||
                meeting.getTime().toLowerCase().contains(lowerCaseSearchString) ||
                meeting.getParticipants().toLowerCase().contains(lowerCaseSearchString)) {
                results.add(meeting);
            }
        }
        return results;
    }

    public static ArrayList<Integer> getMeetingIndex(String titleSearch, String dateSearch, String dateHint) {
        ArrayList<Integer> index = new ArrayList<>();
        String title = titleSearch.toLowerCase().trim();

        for (int i = 0; i < meetings.size(); i++) {
            Meeting meeting = meetings.get(i);
            boolean titleMatches = meeting.getTitle().toLowerCase().contains(title);
            boolean dateMatches = dateSearch.equals(dateHint) || meeting.getDate().equals(dateSearch);

            if (titleMatches && dateMatches) {
                index.add(i);
            }
        }
        return index;
    }

    public static void updateMeeting(int index, Meeting meeting) {
            meetings.set(index, meeting);
    }

    public static void deleteMeeting(int index) {
            meetings.remove(index);
    }

    public static void sortMeetings() {
        meetings.sort(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting m1, Meeting m2) {
                String[] parts1 = m1.getDate().split("\\.");
                String[] parts2 = m2.getDate().split("\\.");

                int yearCompare = parts1[2].compareTo(parts2[2]);
                if (yearCompare != 0) {
                    return yearCompare;
                }

                int monthCompare = parts1[1].compareTo(parts2[1]);
                if (monthCompare != 0) {
                    return monthCompare;
                }

                int dayCompare = parts1[0].compareTo(parts2[0]);
                if (dayCompare != 0) {
                    return dayCompare;
                }

                return m1.getTime().compareTo(m2.getTime());
            }
        });
    }

    public static void initializeMockData() {
        if (meetings.isEmpty()) {
            meetings.add(new Meeting("Work", "Office", "Team", "01.02.2024", "09:00"));
            meetings.add(new Meeting("Work", "Zoom", "Client", "02.02.2024", "14:00"));
            meetings.add(new Meeting("Birthday", "Home", "Family", "10.03.2024", "18:00"));
            meetings.add(new Meeting("Birthday", "Restaurant", "Friends", "11.03.2024", "20:00"));
            meetings.add(new Meeting("School", "Classroom A1", "Students", "15.04.2024", "08:15"));
            meetings.add(new Meeting("School", "Library", "Study Group", "16.04.2024", "12:00"));
            meetings.add(new Meeting("Meeting", "Conference Room", "Board", "20.05.2024", "10:30"));
            meetings.add(new Meeting("Meeting", "Cafe", "Partner", "21.05.2024", "15:45"));
            meetings.add(new Meeting("Work", "Office", "Manager", "05.06.2024", "11:00"));
            meetings.add(new Meeting("School", "Auditorium", "All Students", "01.09.2024", "13:00"));
        }
    }
}
