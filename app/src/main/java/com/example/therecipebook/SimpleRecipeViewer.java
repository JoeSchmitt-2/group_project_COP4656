package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

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

//A generic Listview used in different ways to display recipes
public class SimpleRecipeViewer extends AppCompatActivity {

    private String username;
    private String displayMode;
    private ListView display;
    private User user;

    private Button backFromViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recipe_viewer);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<String> savedRecipes = new ArrayList<>();
        display = findViewById(R.id.displayLV);
        backFromViewButton = findViewById(R.id.backFromViewButton);
        if (bundle != null) {
            username = bundle.getString("user");
            user = getUserFromDatabase(username);
            displayMode = bundle.getString("SRV");
            if (displayMode.equals("userSavedRecipes")) {
                user = getUserFromDatabase(username);
                if(!user.getSavedRecipes().equals(""))
                    savedRecipes = parseUserSavedRecipes(user.getSavedRecipes());
            } else if (displayMode.equals("searchRecipe")) {
                savedRecipes = bundle.getStringArrayList("recipeNames");
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, savedRecipes);
            display.setAdapter(adapter);
            display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String recipeName = parent.getItemAtPosition(position).toString();
                    String selection = "name=?";
                    Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
                    int count = cursor.getCount();

                    cursor.moveToNext();
                    String format = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT));
                    //Debug Toast
                    //Toast.makeText(getApplicationContext(), recipeName +" "+format, Toast.LENGTH_SHORT).show();

                    cursor.close();

                    if (count == 1) {
                        Bundle bund = new Bundle();
                        bund.putString("user", username);
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
        backFromViewButton.setOnClickListener(new View.OnClickListener() {
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


    private User getUserFromDatabase(String username){
        String selection = "username=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);
        cursor.moveToNext();
        User user = new User(cursor.getString(1),cursor.getString(3),cursor.getString(4));
        cursor.close();
        return user;
    }
    private ArrayList<String> parseUserSavedRecipes(String savedRecipes){

        String[] values = savedRecipes.split("-");
        return new ArrayList<>(Arrays.asList(values));
    }


}