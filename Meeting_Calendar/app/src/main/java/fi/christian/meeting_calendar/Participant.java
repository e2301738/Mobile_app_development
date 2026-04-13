package fi.christian.meeting_calendar;


public class Participant {
    private String name;
    private String imagePath;

    public Participant(String name) {
        this.name = name;
        this.imagePath = null;
    }

    public Participant(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return name;
    }
}
