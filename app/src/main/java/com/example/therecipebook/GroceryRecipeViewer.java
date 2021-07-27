package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

//First listView showing Recipes in the GroceryList
public class GroceryRecipeViewer extends AppCompatActivity {

    private ListView groceryList;
    private Button backFromGroceryList;
    private Button getGroceryButton;
    private ArrayList<String> groceryListRecipes;
    private Button gDeleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_recipe_viewer);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        groceryList = findViewById(R.id.groceryLV);
        backFromGroceryList = findViewById(R.id.backFromGroceryList);
        getGroceryButton = findViewById(R.id.getGroceryButton);
        gDeleteButton = findViewById(R.id.deleteGListButton);

        backFromGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });

        //Clears the GroceryList
        gDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = getUserFromDatabase(bundle.getString("user"));
                user.setGroceryListRecipes("");
                updateUserGInDatabase(user);
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });

        //Goes to second listView
        getGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), GroceryIngredients.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        groceryListRecipes = parseUserSavedRecipes(((User) getUserFromDatabase(bundle.getString("user"))).getGroceryListRecipes());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, groceryListRecipes);
        groceryList.setAdapter(adapter);
        //Allows one to be able to view the Recipe if they click on it
        groceryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipeName = parent.getItemAtPosition(position).toString();
                String selection = "name=?";
                Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
                int count = cursor.getCount();

                cursor.moveToNext();
                String format = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT));
                //Toast.makeText(getApplicationContext(), recipeName + " " + format, Toast.LENGTH_SHORT).show();

                cursor.close();

                if (count == 1) {
                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    bund.putString("recipeName", recipeName);
                    Intent intent = null;
                    if (format.equals("text"))
                        intent = new Intent(view.getContext(), ViewTextRecipe.class);
                    else if (format.equals("url"))
                        intent = new Intent(view.getContext(), ViewURLRecipe.class);

                    assert intent != null;
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            }
        });
    }

    //Helpers

    private ArrayList<String> parseUserSavedRecipes(String savedRecipes){
        if(savedRecipes.equals(""))
            return new ArrayList<>();
        if(!savedRecipes.contains("-")){
            ArrayList<String> toReturn = new ArrayList<>();
            toReturn.add(savedRecipes);
            return toReturn;
        }
        String[] values = savedRecipes.split("-");
        return new ArrayList<>(Arrays.asList(values));
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

    private void updateUserGInDatabase(User user){
        String selection = "username=?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeBookContentProvider.COLUMN_GROCERYLISTRECIPES, user.getGroceryListRecipes());
        getApplication().getContentResolver().update(RecipeBookContentProvider.CONTENT_URI_U, contentValues, selection, new String[]{user.getUsername()});

    }
}


