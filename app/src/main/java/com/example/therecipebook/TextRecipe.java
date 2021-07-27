package com.example.therecipebook;

public class TextRecipe extends Recipe {

    private Integer cookTime;
    private String ingredients;
    private String instructions;

    public TextRecipe(String recipeName){
        this.name = recipeName;
        this.format = "text";
        this.cuisine = "";
        this.notes = "";
        this.description = "";
        this.ingredients = "";
        this.instructions = "";
        this.cookTime = 0;
    }

    public TextRecipe(String recipeName, String format,String cuisine, String notes, String description,
                      String ingredients, String instructions, int cookTime){
        this.name = recipeName;
        this.format = format;
        this.cuisine = cuisine;
        this.notes = notes;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.cookTime = cookTime;
    }



    public Integer getCookTime() {
        return cookTime;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

}