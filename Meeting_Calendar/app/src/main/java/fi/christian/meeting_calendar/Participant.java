package fi.christian.meeting_calendar;

import android.graphics.Bitmap;

public class Participant {
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
