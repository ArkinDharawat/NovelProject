package com.hfad.novelproject;

/**
 * Created by arkin on 04/12/16.
 */

public class ChatItem {
    public String author;
    public String line;

    ChatItem(){
        this.author = "";
        this.line = "";
    }

    ChatItem(String name,String chatLine) {
        this.author = name;
        this.line = chatLine;
    }
}
