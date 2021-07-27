package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

//The "Main-menu" that will allow a user to navigate their profile and the database
public class UserProfile extends AppCompatActivity {

    private TextView profileUsernameTV;
    private Button savedRecipesButton;
    private Button recipeSearchButton;
    private Button saveNewRecipeButton;
    private Button groceryListButton;
    private Button inboxButton;
    private Button randomRecipeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setContentView(R.layout.activity_user_profile);
        if(bundle!=null) {
            profileUsernameTV = findViewById(R.id.profileUsernameTV);
            String textToDisplay = bundle.getString("user") + "'s Profile:";
            profileUsernameTV.setText(textToDisplay);
            savedRecipesButton = findViewById(R.id.savedRecipesButton);
            recipeSearchButton = findViewById(R.id.recipeSearchButton);
            saveNewRecipeButton = findViewById(R.id.saveNewRecipeButton);
            groceryListButton = findViewById(R.id.groceryListButton);
            inboxButton = findViewById(R.id.inboxButton);
            randomRecipeButton = findViewById(R.id.randomRecipeButton);

            savedRecipesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), SimpleRecipeViewer.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

            recipeSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), SearchRecipe.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

            saveNewRecipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), SaveNewRecipe.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

            groceryListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), GroceryRecipeViewer.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

            inboxButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), SimpleRecipeViewer.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

            randomRecipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));

                    Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, null, null, null);
                    int count = cursor.getCount();
                    //Debug Toast
                    //Toast.makeText(getApplicationContext(), Integer.valueOf(cursor.getCount()).toString(), Toast.LENGTH_LONG).show();
                    cursor.close();
                    Random random = new Random();
                    int randIndex = random.nextInt(count);
                    cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, null, null, null);
                    for(int i=-1; i<randIndex;i++)
                        cursor.moveToNext();
                    String randRecipeName = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NAME));
                    String randRecipeType = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT));

                    if(randRecipeType.equals("text")) {
                        bund.putString("recipeName", randRecipeName);
                        Intent intent = new Intent(v.getContext(), ViewTextRecipe.class);
                        intent.putExtras(bund);
                        startActivity(intent);
                    }
                    else if(randRecipeType.equals("url")){
                        bund.putString("recipeName", randRecipeName);
                        Intent intent = new Intent(v.getContext(), ViewURLRecipe.class);
                        intent.putExtras(bund);
                        startActivity(intent);
                    }
                }
            });

        }
    }
}