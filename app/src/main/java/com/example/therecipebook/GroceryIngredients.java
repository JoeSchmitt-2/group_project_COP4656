package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

//The second list view showing ingredients in the Recipes in the GroceryList
public class GroceryIngredients extends AppCompatActivity {

    private ListView display;
    private String username;

    private Button backFromIngButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_ingredients);
        backFromIngButton = findViewById(R.id.backFromIngButton);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        display = findViewById(R.id.ingredientsLV);
        ArrayList<String> ingredients = new ArrayList<>();

        if (bundle != null) {
            username = bundle.getString("user");
            User user = getUserFromDatabase(username);
            String recipes = user.getGroceryListRecipes();
            Toast.makeText(getApplicationContext(), user.getGroceryListRecipes(), Toast.LENGTH_LONG).show();
            Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, null, null, null);
            while (cursor.moveToNext()) {
                String recipeName = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NAME));
                String ingredientsToAdd = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INGREDIENTS));
                if(recipes.contains(recipeName)) {
                    ArrayList<String> ing = parseIngredients(ingredientsToAdd);
                    ingredients.addAll(ing);
                }
            }
            cursor.close();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ingredients);
            display.setAdapter(adapter);
            backFromIngButton.setOnClickListener(new View.OnClickListener() {
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

    private ArrayList<String> parseIngredients(String ingredients){

        String[] values = ingredients.split("(\\s)*,(\\s)*");
        return new ArrayList<>(Arrays.asList(values));
    }

    private User getUserFromDatabase(String username){
        String selection = "username=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);
        cursor.moveToNext();
        User user = new User(cursor.getString(1),cursor.getString(3),cursor.getString(4));
        cursor.close();
        return user;
    }
}