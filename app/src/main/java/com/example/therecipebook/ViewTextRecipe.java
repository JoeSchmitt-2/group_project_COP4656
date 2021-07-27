package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ViewTextRecipe extends AppCompatActivity {
    ArrayList<String> randRecipe = new ArrayList<String>();
    SQLiteDatabase db;
    RecipeBookContentProvider.MainDatabaseHelper mainDatabaseHelper = new RecipeBookContentProvider.MainDatabaseHelper(this);
    TextView recipeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_recipe);
        db = mainDatabaseHelper.getReadableDatabase();
        recipeTV = findViewById(R.id.randRecipeName);

        //grabs all the recipes by name
        Cursor cursor = db.rawQuery("SELECT * FROM  Recipes",null);
        randRecipe.clear();
        if (cursor.moveToFirst()) {
            do {
                randRecipe.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        //see how many recipes are in the database and generate a random
        //number within the range
        int numRecipes = cursor.getCount();
        Random random = new Random();
        int randNum = random.nextInt(numRecipes);
        recipeTV.setText(randRecipe.get(randNum));
    }
}
