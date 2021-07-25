package com.example.therecipebook;

public abstract class Recipe {

    private String format;
    private String name;
    private String cuisine;
    private String description;
    private String[] notes;


    public String getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getDescription() {
        return description;
    }

    public String[] getNotes() {
        return notes;
    }
}

