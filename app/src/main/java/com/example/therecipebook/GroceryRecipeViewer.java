package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class GroceryRecipeViewer extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase db;
    Button backButton, getListButton;
    RecipeBookContentProvider.MainDatabaseHelper mainDatabaseHelper = new RecipeBookContentProvider.MainDatabaseHelper(this);
    private ArrayList<String> names = new ArrayList<String>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_recipe_viewer);
        backButton = (Button) findViewById(R.id.backButtonGL);
        getListButton = (Button) findViewById(R.id.getGLButton);
        lv = (ListView) findViewById(R.id.listViewName);
        backButton.setOnClickListener(this);
        getListButton.setOnClickListener(this);
        Bundle bundle = new Bundle();
        displayData();

        //brings the user back to the user profile screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
    }

    //pulls from the ingredients column and populates the listview
    private void displayData() {
        db = mainDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  Recipes",null);
        names.clear();
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        CustomAdapter ca = new CustomAdapter(GroceryRecipeViewer.this, names);
        lv.setAdapter(ca);
        cursor.close();
    }

    //starts the intent when the button is pressed and shows the listview
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.getGLButton)
        {
            Intent intent=new Intent(this,ShowdataListview.class);
            startActivity(intent);
        }
    }
}

