package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            saveRecipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recipeFormat = "";
                    if(textFormatButton.isChecked())
                        recipeFormat = "text";
                    else if(urlFormatButton.isChecked())
                        recipeFormat = "url";
                    recipeName = recipeNameET.getText().toString();
                    Pattern p = Pattern.compile("[A-Za-z0-9\\s]{0,20}");
                    Matcher m = p.matcher(recipeName);
                    if(!m.matches())
                    {
                        Toast.makeText(getApplicationContext(), "Please ensure the Recipe Name is no more than 20 Alphanumeric Characters!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    cuisine = cuisineET.getText().toString().toLowerCase();
                    p = Pattern.compile("[A-Za-z\\s]{0,20}");
                    m = p.matcher(cuisine);
                    if(!m.matches())
                    {
                        Toast.makeText(getApplicationContext(), "Please ensure the Cuisine Type is no more than 20 Alphabetic Characters!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    description = descriptionET.getText().toString();
                    p = Pattern.compile("[A-Za-z0-9\\s\\!\\.\\?\\:]{0,500}");
                    m = p.matcher(description);
                    if(!m.matches())
                    {
                        Toast.makeText(getApplicationContext(), "Please ensure the Description is no more than 500 Alphanumeric Characters or punctuation marks(.?:!)!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(recipeFormat.equals("") || recipeName.equals("")
                            || cuisine.equals("") || description.equals(""))
                        Toast.makeText(getApplicationContext(), "Please ensure no fields are left blank!", Toast.LENGTH_LONG).show();
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
            });
        }
    }
}