package com.example.therecipebook;

public class User {
    String username;
    String[] savedRecipes;
    String[] groceryListRecipes;
    Message[] inboxMessages;

    public User(String username, String[] savedRecipes, String[] groceryListRecipes, Message[] inboxMessages)
    {
        this.username = username;
        this.savedRecipes = savedRecipes;
        this.groceryListRecipes = groceryListRecipes;
        this.inboxMessages = inboxMessages;
    }


}


class Message{
    String sender;
    String message;
    String recipeName;

    public Message(String sender, String message, String recipeName)
    {
        this.sender = sender;
        this.message = message;
        this.recipeName = recipeName;
    }
}
