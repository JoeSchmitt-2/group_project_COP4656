package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchRecipe extends AppCompatActivity {

    private EditText cuisineSearchET;
    private EditText cookTimeSearchET;
    private EditText ingredientsSearchET;
    private Spinner recipeFormatSpinner;

    private Button backFromSearchButton;
    private Button searchButton;

    private String recipeFormat;
    private String cuisine;
    private int cookTime;
    private String ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        recipeFormatSpinner = findViewById(R.id.recipeFormatSpinner);
        cuisineSearchET = findViewById(R.id.cuisineSearchET);
        cookTimeSearchET = findViewById(R.id.cookTimeSearchET);
        ingredientsSearchET = findViewById(R.id.ingredientsSearchET);
        searchButton = findViewById(R.id.searchButton);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        backFromSearchButton = findViewById(R.id.backFromSearchButton);

        recipeFormat = "";
        recipeFormatSpinner.setOnItemSelectedListener(spinnerListener);

        //Performs a DB query based on user input to all of the searchRecipe forms
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisine = cuisineSearchET.getText().toString().trim();
                Pattern p = Pattern.compile("[A-Za-z\\s]{0,20}");
                Matcher m = p.matcher(cuisine);
                if(!m.matches())
                {
                    Toast.makeText(getApplicationContext(), "Cuisine Type cannot be more than 20 Alphabetic Characters!", Toast.LENGTH_LONG).show();
                    return;
                }
                cookTime = Integer.parseInt(cookTimeSearchET.getText().toString().trim());

                ingredients = ingredientsSearchET.getText().toString().trim();
                //Single ingredient OR Single ingredient, Single ingredient (,Single ingredient)*
                p = Pattern.compile("[A-Za-z\\s0-9\\.\\/]+|([A-Za-z\\s0-9\\.\\/]+,([A-Za-z\\s0-9\\.\\/]+)(,[A-Za-z\\s0-9\\.\\/]+)*)");
                m = p.matcher(ingredients);
                if(!m.matches() && !ingredients.equals("All Ingredients\n\n(Please enter items separated with a comma)"))
                {
                    Toast.makeText(getApplicationContext(), "Please enter Ingredients as a comma delimited list!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(recipeFormat.equals("Text") && !cuisine.equals("")
                        && cookTime >= 0 && !ingredients.equals(""))
                {
                    try {
                        String selection = "format=? AND cuisine=? AND cookTime<=?";
                        Cursor cursor;
                        if(cuisine.equals("All Cuisines") && cookTime==0){
                            selection = "format=?";
                            cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase()}, null);
                            //Toast.makeText(getApplicationContext(), "Nothing:"+cursor.getCount(), Toast.LENGTH_SHORT).show();
                        }
                        else if(cookTime == 0)
                        {
                            selection = "format=? AND cuisine=?";
                            cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(),cuisine.toLowerCase()}, null);
                            ///Toast.makeText(getApplicationContext(), "No Cooktime:"+cuisine, Toast.LENGTH_SHORT).show();

                        }
                        else if(cuisine.equals("All Cuisines")){
                            selection = "format=? AND cookTime<=?";
                            cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(),Integer.valueOf(cookTime).toString()}, null);
                            //Toast.makeText(getApplicationContext(), "No Cuisine:"+Integer.valueOf(cookTime).toString(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(), cuisine, Integer.valueOf(cookTime).toString()}, null);
                            //Toast.makeText(getApplicationContext(), "Everything:"+cursor.getCount(), Toast.LENGTH_SHORT).show();

                        }

                        int count = cursor.getCount();
                        cursor.close();

                        if(count > 0) {
                            ArrayList<TextRecipe> recipesToSearch = new ArrayList<>();
                            ArrayList<TextRecipe> recipesToDisplay = new ArrayList<>();

                            selection = "format=? AND cuisine=? AND cookTime<=?";
                            if(cuisine.equals("All Cuisines") && cookTime==0){
                                selection = "format=?";
                                cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase()}, null);
                                //Toast.makeText(getApplicationContext(), "Nothing2:"+cursor.getCount(), Toast.LENGTH_SHORT).show();
                            }
                            else if(cookTime == 0)
                            {
                                selection = "format=? AND cuisine=?";
                                cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(),cuisine.toLowerCase()}, null);
                                //Toast.makeText(getApplicationContext(), "No Cooktime2:"+cuisine, Toast.LENGTH_SHORT).show();

                            }
                            else if(cuisine.equals("All Cuisines")){
                                selection = "format=? AND cookTime<=?";
                                cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(),Integer.valueOf(cookTime).toString()}, null);
                                //Toast.makeText(getApplicationContext(), "No Cuisine2:"+Integer.valueOf(cookTime).toString(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(), cuisine, Integer.valueOf(cookTime).toString()}, null);
                                //Toast.makeText(getApplicationContext(), "Everything2:"+cursor.getCount(), Toast.LENGTH_SHORT).show();
                            }



                            for(int i = 0; i<count; i++){
                                cursor.moveToNext();
                                recipesToSearch.add(new TextRecipe(cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NAME)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_CUISINE)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NOTES)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_DESCRIPTION)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INGREDIENTS)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INSTRUCTIONS)),
                                        cursor.getInt(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_COOKTIME)) ) );
                            }
                            cursor.close();
                            if(!ingredients.equals("All Ingredients\n\n(Please enter items separated with a comma)")) {
                                ArrayList<String> ingredientsToCheck = parseIngredients(ingredients);
                                for (int i = 0; i < recipesToSearch.size(); i++) {
                                    for (int j = 0; j < ingredientsToCheck.size(); j++) {
                                        if (recipesToSearch.get(i).getIngredients().toLowerCase().contains(ingredientsToCheck.get(j).toLowerCase())) {
                                            recipesToDisplay.add(recipesToSearch.get(i));
                                            break;
                                        }
                                    }
                                }
                                //Start new activity with recipesToDisplay
                                ArrayList<String> recipeNames = new ArrayList<>();
                                for(int i = 0; i < recipesToDisplay.size(); i++){
                                    recipeNames.add(recipesToDisplay.get(i).getName());
                                }
                                Bundle bund = new Bundle();
                                bund.putStringArrayList("recipeNames", recipeNames);
                                bund.putString("SRV", "searchRecipe");
                                bund.putString("user", bundle.getString("user"));
                                Intent intent = new Intent(v.getContext(), SimpleRecipeViewer.class);
                                intent.putExtras(bund);
                                startActivity(intent);
                            }
                            else {
                                //Start new activity with recipesToCheck
                                ArrayList<String> recipeNames = new ArrayList<>();
                                for(int i = 0; i < recipesToSearch.size(); i++){
                                    recipeNames.add(recipesToSearch.get(i).getName());
                                }
                                Bundle bund = new Bundle();
                                bund.putStringArrayList("recipeNames", recipeNames);
                                bund.putString("SRV", "searchRecipe");
                                bund.putString("user", bundle.getString("user"));
                                Intent intent = new Intent(v.getContext(), SimpleRecipeViewer.class);
                                intent.putExtras(bund);
                                startActivity(intent);
                            }
                        }
                    }
                    catch (Exception e){
                        //Toast.makeText(getApplicationContext(), "EXCEPTION", Toast.LENGTH_LONG).show();
                    }
                }
                else if(recipeFormat.equals("URL") && !cuisine.equals("")){
                    //CASE FOR SEARCHING URL RECIPES
                    try {
                        String selection = "format=? AND cuisine=?";
                        Cursor cursor;
                        if (cuisine.equals("All Cuisines")) {
                            selection = "format=?";
                            cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase()}, null);
                        } else {
                            cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(), cuisine.toLowerCase()}, null);
                            Toast.makeText(getApplicationContext(), "No Cuisine:" + Integer.valueOf(cookTime).toString(), Toast.LENGTH_SHORT).show();
                        }

                        int count = cursor.getCount();
                        cursor.close();

                        if(count>0) {
                            ArrayList<URLRecipe> recipesToDisplay = new ArrayList<>();
                            selection = "format=? AND cuisine=?";
                            if (cuisine.equals("All Cuisines")) {
                                selection = "format=?";
                                cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase()}, null);
                            } else {
                                cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeFormat.toLowerCase(), cuisine.toLowerCase()}, null);
                                Toast.makeText(getApplicationContext(), "No Cuisine:" + Integer.valueOf(cookTime).toString(), Toast.LENGTH_SHORT).show();
                            }

                            for(int i = 0; i<count; i++){
                                cursor.moveToNext();
                                recipesToDisplay.add(new URLRecipe(cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NAME)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_CUISINE)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NOTES)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_DESCRIPTION)),
                                        cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_URL))));
                            }
                            cursor.close();
                            ArrayList<String> recipeNames = new ArrayList<>();
                            for(int i = 0; i < recipesToDisplay.size(); i++){
                                recipeNames.add(recipesToDisplay.get(i).getName());
                            }
                            Bundle bund = new Bundle();
                            bund.putStringArrayList("recipeNames", recipeNames);
                            bund.putString("SRV", "searchRecipe");
                            bund.putString("user", bundle.getString("user"));
                            Intent intent = new Intent(v.getContext(), SimpleRecipeViewer.class);
                            intent.putExtras(bund);
                            startActivity(intent);
                        }


                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "EXCEPTION", Toast.LENGTH_LONG).show();

                    }
                }
                else
                    if(cookTime>=0)
                        Toast.makeText(getApplicationContext(), "Please ensure no fields are left blank!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Please enter a valid cook time!", Toast.LENGTH_LONG).show();

            }
        });

        backFromSearchButton.setOnClickListener(new View.OnClickListener() {
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


    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            recipeFormat = ((TextView) view).getText().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(),"You didn't select anything" ,Toast.LENGTH_SHORT).show();
        }
    };

    private ArrayList<String> parseIngredients(String ingredients){

        String[] values = ingredients.split("(\\s)*,(\\s)*");
        return new ArrayList<>(Arrays.asList(values));
    }






}