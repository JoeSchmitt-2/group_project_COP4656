package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveTextRecipe extends AppCompatActivity {

    private Button saveTextRecipeButton;

    private EditText cookTimeET;
    private EditText ingredientsET;
    private EditText instructionsET;
    private EditText notesET;

    private String username;
    private String recipeFormat;
    private String recipeName;
    private String cuisine;
    private String description;
    private int cookTime;
    private String ingredients;
    private String instructions;
    private String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_text_recipe);
        saveTextRecipeButton = findViewById(R.id.saveTextRecipeButton);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            username = bundle.getString("user");
            recipeFormat = bundle.getString("format");
            recipeName = bundle.getString("recipeName");
            cuisine = bundle.getString("cuisine");
            description = bundle.getString("description");
        }

        cookTimeET = findViewById(R.id.cookTimeET);
        ingredientsET = findViewById(R.id.ingredientsSaveET);
        instructionsET = findViewById(R.id.instructionsSaveET);
        notesET = findViewById(R.id.notesSaveET);

        //Checks input and enters the recipe into database if valid
        saveTextRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Error Checking
                try {
                    cookTime = Integer.parseInt(cookTimeET.getText().toString());
                }catch (Exception e){
                    //Debug Toast
                    //Toast.makeText(getApplicationContext(), "INT ERROR", Toast.LENGTH_SHORT).show();
                }
                if(cookTime <= 0){
                    //User Toast
                    Toast.makeText(getApplicationContext(), "Please enter a valid cook time!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ingredients = ingredientsET.getText().toString();
                Pattern p = Pattern.compile("[A-Za-z\\s0-9\\.\\/]+|([A-Za-z\\s0-9\\.\\/]+,([A-Za-z\\s0-9\\.\\/]+)(,[A-Za-z\\s0-9\\.\\/]+)*)");
                Matcher m = p.matcher(ingredients);
                if(!m.matches() && !ingredients.equals("All Ingredients\n\n(Please enter items separated with a comma)"))
                {
                    Toast.makeText(getApplicationContext(), "Please enter Ingredients as a comma delimited list!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(ingredientsET==null || ingredients.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter a valid ingredients list!", Toast.LENGTH_SHORT).show();
                    return;
                }
                instructions = instructionsET.getText().toString();
                p = Pattern.compile("([1-9].\\s?[A-Za-z\\s0-9\\.\\/]+)|(([1-9].\\s?[A-Za-z\\s0-9\\.\\/]+)\n)+");
                m = p.matcher(instructions);
                if(!m.matches())
                {
                    Toast.makeText(getApplicationContext(), "Please enter Instructions in the format: \n#. <Instruction>(new line)\n#. <Instruction>(new line)\n...", Toast.LENGTH_LONG).show();
                    return;
                }
                if(instructionsET==null || instructions.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter a valid instructions list!", Toast.LENGTH_SHORT).show();
                    return;
                }
                notes = notesET.getText().toString();
                p = Pattern.compile("[A-Za-z\\s0-9\\.\\/]+|([A-Za-z\\s0-9\\.\\/]+,([A-Za-z\\s0-9\\.\\/]+)(,[A-Za-z\\s0-9\\.\\/]+)*)");
                m = p.matcher(notes);
                if(!m.matches())
                {
                    Toast.makeText(getApplicationContext(), "Please enter Notes as a comma delimited list!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(notesET==null || notes.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter a valid notes list!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Adding the new Recipe to the Recipe Table in the database
                try {
                    String selection = "name=?";
                    Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
                    int count = cursor.getCount();
                    cursor.close();

                    if(count == 0){
                        TextRecipe recipe = new TextRecipe(recipeName,recipeFormat, cuisine, notes, description, ingredients, instructions, cookTime);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(RecipeBookContentProvider.COLUMN_FORMAT, recipe.getFormat());
                        contentValues.put(RecipeBookContentProvider.COLUMN_NAME, recipe.getName());
                        contentValues.put(RecipeBookContentProvider.COLUMN_DESCRIPTION, recipe.getDescription());
                        contentValues.put(RecipeBookContentProvider.COLUMN_CUISINE, recipe.getCuisine());
                        contentValues.put(RecipeBookContentProvider.COLUMN_COOKTIME, recipe.getCookTime());
                        contentValues.put(RecipeBookContentProvider.COLUMN_INGREDIENTS, recipe.getIngredients());
                        contentValues.put(RecipeBookContentProvider.COLUMN_INSTRUCTIONS, recipe.getInstructions());
                        contentValues.put(RecipeBookContentProvider.COLUMN_URL, "NONE");
                        contentValues.put(RecipeBookContentProvider.COLUMN_NOTES, recipe.getNotes());

                        if (getApplicationContext().getContentResolver().insert(RecipeBookContentProvider.CONTENT_URI_R, contentValues) != null) {
                            Toast.makeText(getApplicationContext(), "Inserted \'" + recipeName + "\' into the Database!\nThank you for your contribution!", Toast.LENGTH_LONG).show();

                            //Adding the new Recipe to the User's Saved Recipes List
                            User user = getUserFromDatabase(username);
                            StringBuilder savedRecipes = new StringBuilder(user.getSavedRecipes());

                            if(savedRecipes.length() == 0){
                                savedRecipes.append(recipeName);
                            }
                            else if(!savedRecipes.toString().contains(recipeName)){
                                savedRecipes.append("-").append(recipeName);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Recipe already saved!", Toast.LENGTH_LONG).show();


                            user.setSavedRecipes(savedRecipes.toString());
                            updateUserInDatabase(user);

                            Bundle bund = new Bundle();
                            bund.putString("user", username);
                            Intent intent = new Intent(v.getContext(), UserProfile.class);
                            intent.putExtras(bund);
                            startActivity(intent);

                        } else
                            Toast.makeText(getApplicationContext(), "Error inserting recipe:\'" + recipeName + "\' into Database!", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "A recipe with that name already exists! Try a different recipe name!", Toast.LENGTH_LONG).show();
                        Bundle bund = new Bundle();
                        bund.putString("user", username);
                        Intent intent = new Intent(v.getContext(), SaveNewRecipe.class);
                        intent.putExtras(bund);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error inserting recipe!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    //Helpers

    private User getUserFromDatabase(String username){
        String selection = "username=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);
        cursor.moveToNext();
        User user = new User(cursor.getString(1),cursor.getString(3),cursor.getString(4));
        cursor.close();
        return user;
    }

    private void updateUserInDatabase(User user){
        String selection = "username=?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeBookContentProvider.COLUMN_SAVEDRECIPES, user.getSavedRecipes());
        getApplication().getContentResolver().update(RecipeBookContentProvider.CONTENT_URI_U, contentValues, selection, new String[]{user.getUsername()});

    }

}