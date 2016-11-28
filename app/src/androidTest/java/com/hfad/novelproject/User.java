package com.hfad.novelproject;

/**
 * Created by arkin on 27/11/16.
 */

public class User {
    public String name;
    public String favFood;

    public User(){
        name = "";
        favFood = "";
    }

    public User(String name, String favFood) {
        this.name = name;
        this.favFood = favFood;
    }
}

