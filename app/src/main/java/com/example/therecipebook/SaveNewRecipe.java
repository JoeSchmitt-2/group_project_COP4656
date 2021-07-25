package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveNewRecipe extends AppCompatActivity {

    private Button textFormatButton;
    private Button urlFormatButton;
    private EditText recipeNameET;
    private EditText cuisineET;
    private EditText descriptionET;
    private Button backFromSaveButton;
    private Button saveRecipeButton;

    private String recipeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_recipe);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            textFormatButton = findViewById(R.id.textFormatButton);
            urlFormatButton = findViewById(R.id.urlFormatButton);
            recipeNameET = findViewById(R.id.recipeNameET);
            cuisineET = findViewById(R.id.cuisineET);
            descriptionET = findViewById(R.id.descriptionET);
            backFromSaveButton = findViewById(R.id.backFromSaveButton);
            saveRecipeButton = findViewById(R.id.saveRecipeButton);
            backFromSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), UserProfile.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });
            saveRecipeButton.setOnClickListener(nextListener);
        }



        recipeFormat = "";
    }


    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


}