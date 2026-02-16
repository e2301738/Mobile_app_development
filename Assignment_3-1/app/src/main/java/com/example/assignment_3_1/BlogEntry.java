package com.example.assignment_3_1;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlogEntry {
    private String name;
    private String comment;
    private String timeStamp;
    private String date;
    private int entryNumber = 0;

    public BlogEntry(String comment, String name) {
        this.comment = comment;
        this.name = name;
        this.entryNumber = +1;

        SimpleDateFormat timeStampDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());

        this.timeStamp = timeStampDate.format(new Date());
        this.date = date.format(new Date());
    }

    public boolean searchByText(String searchText) {
        String lowerCase = searchText.toLowerCase();
        return name.toLowerCase().contains(lowerCase) || comment.toLowerCase().contains(lowerCase);
    }

    public boolean searchByDate(String searchDate) {
        return date.equals(searchDate);
    }

    @Override
    public String toString(){
        return "Entry number: " + entryNumber + "\nUser: " + name + "\nTime: " + timeStamp +
                "\nComment: " + comment;
    }


}
