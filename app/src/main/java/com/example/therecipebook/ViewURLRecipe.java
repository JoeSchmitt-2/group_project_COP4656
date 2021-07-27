package com.example.therecipebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Displays the URL recipe with a WebView,
//with buttons with various features to do with the recipe
public class ViewURLRecipe extends AppCompatActivity {

    WebView webView;

    private TextView recipeTV;
    private Button backFromURLViewButton;
    private Button saveURLToUserButton;
    private Button saveURLToGroceryButton;
    private Button shareURLButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_url_recipe);
        recipeTV = findViewById(R.id.recipeNameURLTV);
        backFromURLViewButton = findViewById(R.id.backFromViewURLButton);
        saveURLToUserButton = findViewById(R.id.saveURLToUserButton);
        saveURLToGroceryButton = findViewById(R.id.urlAddGroceryButton);
        shareURLButton = findViewById(R.id.shareUrlButton);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String recipeName = bundle.getString("recipeName");
        recipeTV.setText(recipeName);
        webView = findViewById(R.id.recipeWebView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        String selection = "name=?";
        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
        int count = cursor.getCount();
        String url = "https://www.google.com";
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            url = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_URL));
        }
        //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();

        String format = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_FORMAT));
        cursor.close();
        webView.loadUrl(url);


        backFromURLViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });

        User user = getUserFromDatabase(bundle.getString("user"));
        String d = "Delete";
        if (user.getSavedRecipes().contains(recipeName)) {
            saveURLToUserButton.setText(d);
            saveURLToUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = getUserFromDatabase(bundle.getString("user"));
                    String savedRecipes = new String(user.getSavedRecipes());
                    savedRecipes = savedRecipes.replace(recipeName + "-", "");
                    user.setSavedRecipes(savedRecipes.toString());

                    updateUserInDatabase(user);
                    Toast.makeText(getApplicationContext(), "Recipe deleted!", Toast.LENGTH_LONG).show();

                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), UserProfile.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });
        } else {
            saveURLToUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = getUserFromDatabase(bundle.getString("user"));
                    StringBuilder savedRecipes = new StringBuilder(user.getSavedRecipes());

                    if (savedRecipes.length() == 0) {
                        savedRecipes.append(recipeName);
                    } else if (!savedRecipes.toString().contains(recipeName)) {
                        savedRecipes.append("-").append(recipeName);
                    } else {
                        Toast.makeText(getApplicationContext(), "Recipe already saved!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    user.setSavedRecipes(savedRecipes.toString());
                    updateUserInDatabase(user);
                    Toast.makeText(getApplicationContext(), "Recipe saved!", Toast.LENGTH_LONG).show();

                    Bundle bund = new Bundle();
                    bund.putString("user", bundle.getString("user"));
                    Intent intent = new Intent(v.getContext(), UserProfile.class);
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });
        }

        saveURLToGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = getUserFromDatabase(bundle.getString("user"));
                StringBuilder glRecipes = new StringBuilder(user.getGroceryListRecipes());

                if (glRecipes.length() == 0) {
                    glRecipes.append(recipeName);
                } else {
                    glRecipes.append("-").append(recipeName);
                }
                user.setGroceryListRecipes(glRecipes.toString());
                updateUserGInDatabase(user);
                Toast.makeText(getApplicationContext(), "Added to Grocery List!", Toast.LENGTH_LONG).show();
            }
        });

        shareURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                bund.putString("format", "url");
                bund.putString("recipeName", recipeName);
                Intent intent = new Intent(v.getContext(), ShareRecipe.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
    }


        private User getUserFromDatabase (String username){
            String selection = "username=?";
            Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);
            cursor.moveToNext();
            User user = new User(cursor.getString(1), cursor.getString(3), cursor.getString(4));
            cursor.close();
            return user;
        }

        private void updateUserInDatabase (User user){
            String selection = "username=?";
            ContentValues contentValues = new ContentValues();
            contentValues.put(RecipeBookContentProvider.COLUMN_SAVEDRECIPES, user.getSavedRecipes());
            getApplication().getContentResolver().update(RecipeBookContentProvider.CONTENT_URI_U, contentValues, selection, new String[]{user.getUsername()});

        }
        private void updateUserGInDatabase (User user){
            String selection = "username=?";
            ContentValues contentValues = new ContentValues();
            contentValues.put(RecipeBookContentProvider.COLUMN_GROCERYLISTRECIPES, user.getGroceryListRecipes());
            getApplication().getContentResolver().update(RecipeBookContentProvider.CONTENT_URI_U, contentValues, selection, new String[]{user.getUsername()});

        }


        @Override
        protected void onPostCreate (@Nullable Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);

        }

}