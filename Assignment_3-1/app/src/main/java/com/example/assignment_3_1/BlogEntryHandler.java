package com.example.assignment_3_1;

import java.util.ArrayList;
import java.util.List;

public class BlogEntryHandler {

    private static List<BlogEntry> blogEntries = new ArrayList<>();

    public void addBlogEntry(BlogEntry blogEntry){
        blogEntries.add(0, blogEntry);
    }

    public List<BlogEntry> getAllBlogEntries(){
        return blogEntries;
    }

    public List<BlogEntry> filterByText(String filterText) {
        List<BlogEntry> foundMatches = new ArrayList<>();
        for (BlogEntry entry : blogEntries) {
            if (entry.searchByText(filterText)) {
                foundMatches.add(entry);
            }
        }
        return foundMatches;
    }

    public List<BlogEntry> filterByDate(String filterDate) {
        List<BlogEntry> foundMatches = new ArrayList<>();
        for (BlogEntry entry : blogEntries) {
            if (entry.searchByDate(filterDate)) {
                foundMatches.add(entry);
            }
        }
        return foundMatches;
    }
}
