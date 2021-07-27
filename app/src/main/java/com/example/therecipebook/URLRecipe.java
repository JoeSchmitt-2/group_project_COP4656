package com.example.therecipebook;

public class URLRecipe extends Recipe{

    private String url;

    public URLRecipe(String recipeName, String recipeFormat, String cuisine, String notes, String description, String url){
        this.name = recipeName;
        this.format = recipeFormat;
        this.cuisine = cuisine;
        this.notes = notes;
        this.description = description;
        this.url = url;


    }

    public String getUrl() {
        return url;
    }
}
