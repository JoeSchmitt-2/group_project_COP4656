package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

public class SimpleRecipeViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recipe_viewer);
    }






    public User getUserFromDatabase(String username){
        String selection = "username=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);


        return null;
    };
}