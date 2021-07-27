package com.example.therecipebook;

public class User {
    String username;
    String savedRecipes;
    String groceryListRecipes;


    public User(String username)
    {
        this.username = username;
        this.savedRecipes = "";
        this.groceryListRecipes = "";
    }

    public User(String username, String savedRecipes, String groceryListRecipes)
    {
        this.username = username;
        this.savedRecipes = savedRecipes;
        this.groceryListRecipes = groceryListRecipes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(String savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public String getGroceryListRecipes() {
        return groceryListRecipes;
    }

    public void setGroceryListRecipes(String groceryListRecipes) {
        this.groceryListRecipes = groceryListRecipes;
    }
}
