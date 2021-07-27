package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GroceryRecipeViewer extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase db;
    Button backButton, getListButton;
    RecipeBookContentProvider.MainDatabaseHelper databaseHelper = new RecipeBookContentProvider.MainDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_recipe_viewer);
        backButton = (Button) findViewById(R.id.backButtonGL);
        getListButton = (Button) findViewById(R.id.getGLButton);
        backButton.setOnClickListener(this);
        getListButton.setOnClickListener(this);
        Bundle bundle = new Bundle();

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
