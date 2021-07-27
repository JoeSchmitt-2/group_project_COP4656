package com.example.therecipebook;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowdataListview extends AppCompatActivity {
    RecipeBookContentProvider.MainDatabaseHelper mainDatabaseHelper = new RecipeBookContentProvider.MainDatabaseHelper(this);
    SQLiteDatabase db;
    private ArrayList<String> ingredients = new ArrayList<String>();
    ListView lv;

    //register the listview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        lv = (ListView) findViewById(R.id.groceryLV);
    }
    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    //pulls from the ingredients column and populates the listview
    private void displayData() {
        db = mainDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  Recipes",null);
        ingredients.clear();
        if (cursor.moveToFirst()) {
            do {
                ingredients.add(cursor.getString(cursor.getColumnIndex("ingredients")));
            } while (cursor.moveToNext());
        }
        CustomAdapter ca = new CustomAdapter(ShowdataListview.this, ingredients);
        lv.setAdapter(ca);
        cursor.close();
    }
}