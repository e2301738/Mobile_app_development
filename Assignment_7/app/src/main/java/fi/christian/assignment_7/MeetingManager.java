package fi.christian.assignment_7;

import java.util.ArrayList;

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

    /**
     * Searches for meetings and returns their original indices in the main list.
     * Used by UpdateActivity to maintain reference to the correct meeting.
     */
    public static ArrayList<Integer> searchUpdateMeetingIndices(String titleQuery, String dateQuery, String dateHint) {
        ArrayList<Integer> foundIndices = new ArrayList<>();
        String lowerTitle = titleQuery.toLowerCase();

        for (int i = 0; i < meetings.size(); i++) {
            Meeting meeting = meetings.get(i);
            boolean titleMatches = meeting.getTitle().toLowerCase().contains(lowerTitle);
            boolean dateMatches = dateQuery.equals(dateHint) || meeting.getDate().equals(dateQuery);

            if (titleMatches && dateMatches) {
                foundIndices.add(i);
            }
        }
        return foundIndices;
    }

    /**
     * Validates if the meeting data is complete and not default hints.
     */
    public static boolean isInputValid(String title, String place, String participants, String date, String time, String dateHint, String timeHint) {
        if (title.trim().isEmpty() || place.trim().isEmpty() || participants.trim().isEmpty()) {
            return false;
        }
        if (date.isEmpty() || date.equalsIgnoreCase(dateHint)) {
            return false;
        }
        if (time.isEmpty() || time.equalsIgnoreCase(timeHint)) {
            return false;
        }
        return true;
    }

    public static void updateMeeting(int index, Meeting meeting) {
        if (index >= 0 && index < meetings.size()) {
            meetings.set(index, meeting);
        }
    }

    public static void deleteMeeting(int index) {
        if (index >= 0 && index < meetings.size()) {
            meetings.remove(index);
        }
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
