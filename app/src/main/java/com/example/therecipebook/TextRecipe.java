package com.example.therecipebook;

public class TextRecipe extends Recipe{

    private Integer cookTime;
    private Ingredients[] ingredients;
    private String[] instructions;


    public Integer getCookTime() {
        return cookTime;
    }

    public Ingredients[] getIngredients() {
        return ingredients;
    }

    public String[] getInstructions() {
        return instructions;
    }


}

class Ingredients {
    Float amount;
    String units;
    String item;

    public Ingredients(Float amount, String units, String item)
    {
        this.amount = amount;
        this.units = units;
        this.item = item;
    }

    public Float getAmount() {
        return amount;
    }

    public String getUnits() {
        return units;
    }

    public String getItem() {
        return item;
    }
}