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
import java.util.regex.*;

public class SaveURLRecipe extends AppCompatActivity {

    private String username;
    private String recipeFormat;
    private String recipeName;
    private String cuisine;
    private String description;
    private String url;
    private String notes;
    private EditText urlET;
    private EditText notesURLSaveET;
    private Button saveURLRecipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_url_recipe);
        saveURLRecipeButton = findViewById(R.id.saveURLRecipeButton);
        urlET = findViewById(R.id.urlET);
        notesURLSaveET = findViewById(R.id.notesURLSaveET);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            username = bundle.getString("user");
            recipeFormat = bundle.getString("format");
            recipeName = bundle.getString("recipeName");
            cuisine = bundle.getString("cuisine");
            description = bundle.getString("description");
        }
        //Saves URL Recipe to database and validates input
        saveURLRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Error Checking
                url = urlET.getText().toString().trim();
                //Reference to where I found the regular expression
                //https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
                Pattern p = Pattern.compile("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})");
                Matcher m = p.matcher(url);
                boolean b = m.matches();
                if(urlET==null || url.equals("") || !b){
                    Toast.makeText(getApplicationContext(), "Please enter a valid url!", Toast.LENGTH_SHORT).show();
                    return;
                }
                notes = notesURLSaveET.getText().toString();
                p = Pattern.compile("[A-Za-z\\s0-9\\.\\/]+|([A-Za-z\\s0-9\\.\\/]+,([A-Za-z\\s0-9\\.\\/]+)(,[A-Za-z\\s0-9\\.\\/]+)*)");
                m = p.matcher(notes);
                if(!m.matches())
                {
                    Toast.makeText(getApplicationContext(), "Please enter Notes as a comma delimited list!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(notesURLSaveET==null || notes.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter a valid notes list!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Adding the new Recipe to the Recipe Table in the database
                try {
                    String selection = "name=?";
                    Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
                    int count = cursor.getCount();
                    cursor.close();
                    //Toast.makeText(getApplicationContext(), "-- \'" + count + "\'!", Toast.LENGTH_LONG).show();

                    if(count == 0){
                        URLRecipe recipe = new URLRecipe(recipeName, recipeFormat, cuisine, notes, description, url);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(RecipeBookContentProvider.COLUMN_FORMAT, recipe.getFormat());
                        contentValues.put(RecipeBookContentProvider.COLUMN_NAME, recipe.getName());
                        contentValues.put(RecipeBookContentProvider.COLUMN_DESCRIPTION, recipe.getDescription());
                        contentValues.put(RecipeBookContentProvider.COLUMN_CUISINE, recipe.getCuisine());
                        contentValues.put(RecipeBookContentProvider.COLUMN_COOKTIME, 0);
                        contentValues.put(RecipeBookContentProvider.COLUMN_INGREDIENTS, "");
                        contentValues.put(RecipeBookContentProvider.COLUMN_INSTRUCTIONS, "");
                        contentValues.put(RecipeBookContentProvider.COLUMN_URL, recipe.getUrl());
                        contentValues.put(RecipeBookContentProvider.COLUMN_NOTES, recipe.getNotes());

                        if (getApplicationContext().getContentResolver().insert(RecipeBookContentProvider.CONTENT_URI_R, contentValues) != null) {
                            Toast.makeText(getApplicationContext(), "Inserted \'" + recipeName + "\' into the Database!\nThank you for your contribution!", Toast.LENGTH_LONG).show();

                            //Adding the new Recipe to the User's Saved Recipes List
                            User user = getUserFromDatabase(username);
                            StringBuilder savedRecipes = new StringBuilder(user.getSavedRecipes());

                            if(savedRecipes.length() == 0){
                                savedRecipes.append(recipeName);
                            }
                            else {
                                savedRecipes.append("-").append(recipeName);
                            }

                            user.setSavedRecipes(savedRecipes.toString());
                            updateUserInDatabase(user);
                            Bundle bund = new Bundle();
                            bund.putString("user", bundle.getString("user"));
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