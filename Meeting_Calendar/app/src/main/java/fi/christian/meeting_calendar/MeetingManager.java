package fi.christian.meeting_calendar;

import java.util.ArrayList;
import java.util.Comparator;

public class MeetingManager {
    private static ArrayList<Meeting> meetings = new ArrayList<>();
    private static ArrayList<Participant> tempParticipants = new ArrayList<>();

    public static void addMeeting(Meeting meeting) {
        for (Meeting m : meetings) {
            if (m.getTitle().equals(meeting.getTitle()) &&
                m.getPlace().equals(meeting.getPlace()) &&
                m.getDate().equals(meeting.getDate()) &&
                m.getTime().equals(meeting.getTime())) {
                return;
            }
        }
        meetings.add(meeting);
        sortMeetings();
    }

    public static void setMeetings(ArrayList<Meeting> newMeetings) {
        meetings = new ArrayList<>(newMeetings);
        sortMeetings();
    }

    public static void clearMeetings() {
        meetings.clear();
    }

    public static ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public static void setTempParticipants(ArrayList<Participant> participants) {
        tempParticipants = participants;
    }

    public static ArrayList<Participant> getTempParticipants() {
        return tempParticipants;
    }

    public static void clearTempParticipants() {
        tempParticipants.clear();
    }

    public static ArrayList<Meeting> searchMeetings(String searchString) {
        ArrayList<Meeting> results = new ArrayList<>();
        String lowerCaseSearchString = searchString.toLowerCase();

        for (Meeting meeting : meetings) {
            boolean matchesParticipant = false;
            for (Participant p : meeting.getParticipants()) {
                if (p.getName().toLowerCase().contains(lowerCaseSearchString)) {
                    matchesParticipant = true;
                    break;
                }
            }

            if (meeting.getTitle().toLowerCase().contains(lowerCaseSearchString) || 
                meeting.getPlace().toLowerCase().contains(lowerCaseSearchString) || 
                meeting.getDate().toLowerCase().contains(lowerCaseSearchString) || 
                meeting.getTime().toLowerCase().contains(lowerCaseSearchString) ||
                matchesParticipant) {
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
            sortMeetings();
    }

    public static void deleteMeeting(int index) {
            meetings.remove(index);
            sortMeetings();
    }

    public static void sortMeetings() {
        meetings.sort(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting m1, Meeting m2) {
                String[] parts1 = m1.getDate().split("\\.");
                String[] parts2 = m2.getDate().split("\\.");
                
                if (parts1.length < 3 || parts2.length < 3) return 0;

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
}
