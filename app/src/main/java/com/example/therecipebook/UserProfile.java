package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        Intent intent =getIntent();
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
                    Intent intent = new Intent(v.getContext(), ViewTextRecipe.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

        }
    }
}