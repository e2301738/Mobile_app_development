package fi.christian.meeting_calendar;

import android.graphics.Bitmap;

public class Participant {
    private long id;
    private String name;
    private Bitmap image;
    private boolean selected = true;

    public Participant(String name) {
        this.name = name;
        this.image = null;
    }

    public Participant(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public Participant(long id, String name, Bitmap image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name;
    }
}
