package com.example.therecipebook;

public abstract class Recipe {

    protected String format;
    protected String name;
    protected String cuisine;
    protected String description;
    protected String notes;


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

    public String getNotes() {
        return notes;
    }
}

