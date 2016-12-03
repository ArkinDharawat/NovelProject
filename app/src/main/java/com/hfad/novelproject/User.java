package com.hfad.novelproject;

import java.util.ArrayList;

/**
 * Created by arkin on 28/11/16.
 */

public class User {
    public String name;
    public ArrayList<String> savedBooks = new ArrayList<>();

    User(){
        name = "none";

    }

    User(String name) {
        this.name = name;


    }
}
