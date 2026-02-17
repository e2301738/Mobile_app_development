package com.example.assignment_3_1;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlogEntry {
    private static int counter = 1;
    private String userName;
    private String comment;
    private String timeStamp;
    private String creationDate;
    private int entryNumber;

    public BlogEntry(String comment, String userName) {
        this.comment = comment;
        this.userName = userName;
        this.entryNumber = counter++;

        SimpleDateFormat timeStampDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat creationDateFormat = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());

        this.timeStamp = timeStampDate.format(new Date());
        this.creationDate = creationDateFormat.format(new Date());
    }

    public boolean searchByText(String searchText) {
        String textToSearch = searchText.toLowerCase();
        return userName.toLowerCase().contains(textToSearch) ||
                comment.toLowerCase().contains(textToSearch);
    }

    public boolean searchByDate(String searchDate) {
        return creationDate.equals(searchDate);
    }

    @Override
    public String toString(){
        return "Entry: " + entryNumber + "\nUser: " + userName + "\nTime: " + timeStamp +
                "\nComment: \n" + comment;
    }


}
