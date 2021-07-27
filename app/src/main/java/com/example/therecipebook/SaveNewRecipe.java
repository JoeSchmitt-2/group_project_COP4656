package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SaveNewRecipe extends AppCompatActivity {

    private RadioButton textFormatButton;
    private RadioButton urlFormatButton;
    private EditText recipeNameET;
    private EditText cuisineET;
    private EditText descriptionET;
    private Button backFromSaveButton;
    private Button saveRecipeButton;

    private String username;
    private String recipeFormat;
    private String recipeName;
    private String cuisine;
    private String description;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_recipe);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            username = bundle.getString("user");
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

    }


    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            recipeFormat = "";
            if(textFormatButton.isChecked())
                recipeFormat = "text";
            else if(urlFormatButton.isChecked())
                recipeFormat = "url";
            recipeName = recipeNameET.getText().toString();
            cuisine = cuisineET.getText().toString();
            description = descriptionET.getText().toString();
            if(recipeFormat.equals("") || recipeName.equals("")
                    || cuisine.equals("") || description.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Please ensure no fields are left Blank!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), SaveNewRecipe.class);
                startActivity(intent);
            }
            else
            {
                if(recipeFormat.equals("text")){
                    Intent intent = new Intent(v.getContext(), SaveTextRecipe.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("user", username);
                    bundle.putString("format", recipeFormat);
                    bundle.putString("recipeName", recipeName);
                    bundle.putString("cuisine", cuisine);
                    bundle.putString("description", description);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (recipeFormat.equals("url")){
                    Intent intent = new Intent(v.getContext(), SaveURLRecipe.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("user", username);
                    bundle.putString("format", recipeFormat);
                    bundle.putString("recipeName", recipeName);
                    bundle.putString("cuisine", cuisine);
                    bundle.putString("description", description);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_LONG).show();

            }
        }
    };


}