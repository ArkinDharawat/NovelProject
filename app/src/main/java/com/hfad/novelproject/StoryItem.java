package com.hfad.novelproject;

/**
 * Created by arkin on 28/11/16.
 */

public class StoryItem {
    public String genre;
    public String storyID;

    StoryItem() {
        genre = "";
        storyID = "";
    }

    StoryItem(String genre,String hexID) {
        this.genre = genre;
        this.storyID = hexID;
    }
}
