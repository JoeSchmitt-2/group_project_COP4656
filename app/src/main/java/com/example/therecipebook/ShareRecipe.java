package com.example.therecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Sends an email form of the recipe to somebody
public class ShareRecipe extends AppCompatActivity {

    private EditText emailAddET;
    private EditText emailMessageET;

    private Button shareRButton;
    private Button backFromShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_recipe);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emailAddET = findViewById(R.id.emailAddET);
        emailMessageET = findViewById(R.id.emailMessageET);
        shareRButton = findViewById(R.id.shareRButton);
        String format = bundle.getString("format");
        String recipeName = bundle.getString("recipeName");
        backFromShareButton = findViewById(R.id.backFromShareButton);
        backFromShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund = new Bundle();
                bund.putString("user", bundle.getString("user"));
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });



        shareRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reference for regex:
                //https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
                String emailAdd = emailAddET.getText().toString().trim();
                String message = emailMessageET.getText().toString().trim();
                Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                Matcher m = p.matcher(emailAdd);
                if(!m.matches())
                {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address!", Toast.LENGTH_LONG).show();
                    return;
                }
                String selection = "name=?";
                Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_R, null, selection, new String[]{recipeName}, null);
                String cuisine, cookTime, desc, ing, instr, notes, url;
                if(format.equals("text")) {
                    if (cursor.getCount() == 1) {
                        cursor.moveToNext();
                        cuisine = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_CUISINE));
                        cookTime = Integer.valueOf(cursor.getInt(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_COOKTIME))).toString();
                        desc = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_DESCRIPTION));
                        ing = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INGREDIENTS));
                        instr = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_INSTRUCTIONS));
                        notes = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NOTES));
                    } else return;
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAdd});
                    i.putExtra(Intent.EXTRA_SUBJECT, recipeName);
                    i.putExtra(Intent.EXTRA_TEXT, message + "\nCuisine: " + cuisine + "\n"
                            + "Cook Time: " + cookTime + "\n"
                            + "Description: " + desc + "\n"
                            + "Ingredients: " + ing + "\n"
                            + "Instructions:\n" + instr + "\n"
                            + "Notes: " + notes + "\n");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(format.equals("url")) {
                    if (cursor.getCount() == 1) {
                        cursor.moveToNext();
                        cuisine = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_CUISINE));
                        desc = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_DESCRIPTION));
                        notes = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_NOTES));
                        url = cursor.getString(cursor.getColumnIndex(RecipeBookContentProvider.COLUMN_URL));
                    } else return;
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAdd});
                    i.putExtra(Intent.EXTRA_SUBJECT, recipeName);
                    i.putExtra(Intent.EXTRA_TEXT, message + "Cuisine: " + cuisine + "\n"
                            + "Description: " + desc + "\n"
                            + "Notes: " + notes + "\n\n" +
                            "Link : " + url);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    }
}