package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SimpleRecipeViewer extends AppCompatActivity {

    private String username;
    private ListView display;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recipe_viewer);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        display = findViewById(R.id.displayLV);
        if (bundle != null) {
            username = bundle.getString("user");
            user = getUserFromDatabase(username);
        }
        ArrayList<String> savedRecipes = new ArrayList<>();
        savedRecipes.add(user.getSavedRecipes());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, savedRecipes );
        display.setAdapter(adapter);
    }


    private User getUserFromDatabase(String username){
        String selection = "username=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);
        cursor.moveToNext();
        User user = new User(cursor.getString(1),cursor.getString(3),cursor.getString(4));
        cursor.close();
        return user;
    }
}