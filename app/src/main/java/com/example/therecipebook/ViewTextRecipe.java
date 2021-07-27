package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

//Displays the text recipe in a LinearLayout format and is scrollable based on size,
//with buttons with various features to do with the recipe
public class ViewTextRecipe extends AppCompatActivity {

    private String recipeName;
    private TextView recipeNameTV;
    private TextView cuisineTV;
    private TextView cookTimeTV;
    private TextView descriptionTV;
    private LinearLayout ingrLayout;
    private LinearLayout instrLayout;
    private LinearLayout notesLayout;

    private Button backFromViewTextButton;
    private Button saveTextToUserButton;
    private Button saveTextToGroceryButton;
    private Button shareTextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_recipe);
        ingrLayout = findViewById(R.id.ingredientsTV);
        instrLayout = findViewById(R.id.instructionsTV);
        notesLayout = findViewById(R.id.notesTV);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeName = bundle.getString("recipeName");
        String username = bundle.getString("user");
        recipeNameTV = findViewById(R.id.recipeNameTV);
        cookTimeTV = findViewById(R.id.cookTimeTV);
        cuisineTV = findViewById(R.id.cuisineTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        backFromViewTextButton = findViewById(R.id.backFromViewTextButton);
        saveTextToUserButton = findViewById(R.id.saveTextRecipeToUserButton);
        saveTextToGroceryButton = findViewById(R.id.saveTextToGroceryButton);
        shareTextButton = findViewById(R.id.shareTextButton);
        String selection = "name=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
        if(cursor.getCount() == 1){
            cursor.moveToNext();
            TextRecipe recipeToDisplay = new TextRecipe(cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT)),
                    cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_CUISINE)),
                    cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INGREDIENTS)),
                    cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INSTRUCTIONS)),
                    cursor.getInt(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_COOKTIME)));

            recipeNameTV.setText(recipeToDisplay.getName());
            cuisineTV.setText(recipeToDisplay.getCuisine());
            descriptionTV.setText(recipeToDisplay.getDescription());
            String cookTimeDisplay = recipeToDisplay.getCookTime() + " minutes";
            cookTimeTV.setText(cookTimeDisplay);

            ArrayList<String> ingredients = parseIngredients(recipeToDisplay.getIngredients());
            ArrayList<String> instructions = parseInstructions(recipeToDisplay.getInstructions());
            ArrayList<String> notes = parseIngredients(recipeToDisplay.getNotes());

            for(String ingredient: ingredients){
                TextView toAdd = new TextView(this);
                toAdd.setText(ingredient);
                toAdd.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ingrLayout.addView(toAdd);
            }
            for(String instruction: instructions){
                TextView toAdd = new TextView(this);
                toAdd.setText(instruction);
                toAdd.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                instrLayout.addView(toAdd);
            }
            for(String note: notes){
                TextView toAdd = new TextView(this);
                toAdd.setText(note);
                toAdd.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                notesLayout.addView(toAdd);
            }


        }
        cursor.close();


        backFromViewTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        User user = getUserFromDatabase(username);
        String d = "Delete";
        if(user.getSavedRecipes().contains(recipeName)) {
            saveTextToUserButton.setText(d);
            saveTextToUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = getUserFromDatabase(bundle.getString("user"));
                    String savedRecipes = new String(user.getSavedRecipes());
                    savedRecipes = savedRecipes.replace(recipeName+"-", "");
                    user.setSavedRecipes(savedRecipes.toString());

                    updateUserInDatabase(user);
                    Toast.makeText(getApplicationContext(), "Recipe deleted!", Toast.LENGTH_LONG).show();

                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), UserProfile.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });
        }else {
            saveTextToUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = getUserFromDatabase(bundle.getString("user"));
                    StringBuilder savedRecipes = new StringBuilder(user.getSavedRecipes());

                    if (savedRecipes.length() == 0) {
                        savedRecipes.append(recipeName);
                    } else if (!savedRecipes.toString().contains(recipeName)) {
                        savedRecipes.append("-").append(recipeName);
                    } else {
                        Toast.makeText(getApplicationContext(), "Recipe already saved!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    user.setSavedRecipes(savedRecipes.toString());
                    updateUserInDatabase(user);
                    Toast.makeText(getApplicationContext(), "Recipe saved!", Toast.LENGTH_LONG).show();

                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), UserProfile.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });
        }
        saveTextToGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = getUserFromDatabase(bundle.getString("user"));
                StringBuilder glRecipes = new StringBuilder(user.getGroceryListRecipes());

                if(glRecipes.length() == 0){
                    glRecipes.append(recipeName);
                }
                else {
                    glRecipes.append("-").append(recipeName);
                }
                user.setGroceryListRecipes(glRecipes.toString());
                updateUserGInDatabase(user);
                Toast.makeText(getApplicationContext(), "Added to Grocery List!", Toast.LENGTH_LONG).show();
            }
        });


        shareTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                bund.putString("format", "text");
                bund.putString("recipeName", recipeName);
                Intent intent = new Intent(v.getContext(), ShareRecipe.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });


    }

    private ArrayList<String> parseIngredients(String ingredients){

        String[] values = ingredients.split("(\\s)*,(\\s)*");
        return new ArrayList<>(Arrays.asList(values));
    }
    private ArrayList<String> parseInstructions(String ingredients){

        String[] values = ingredients.split("\n");
        return new ArrayList<>(Arrays.asList(values));
    }

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

    private void updateUserGInDatabase(User user){
        String selection = "username=?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeBookContentProvider.COLUMN_GROCERYLISTRECIPES, user.getGroceryListRecipes());
        getApplication().getContentResolver().update(RecipeBookContentProvider.CONTENT_URI_U, contentValues, selection, new String[]{user.getUsername()});

    }


}