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

    public List<BlogEntry> searchByText(String searchText) {
        List<BlogEntry> results = new ArrayList<>();
        for (BlogEntry entry : blogEntries) {
            if (entry.searchByText(searchText)) {
                results.add(entry);
            }
        }
        return results;
    }

    public List<BlogEntry> searchByDate(String searchDate) {
        List<BlogEntry> results = new ArrayList<>();
        for (BlogEntry entry : blogEntries) {
            if (entry.searchByDate(searchDate)) {
                results.add(entry);
            }
        }
        return results;
    }
}
