package com.example.therecipebook;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity {

    private EditText newUsernameET;
    private EditText newPasswordET;
    private Button createAccountButton;
    private Button backToLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        newUsernameET = findViewById(R.id.newUsernameET);
        newPasswordET = findViewById(R.id.newPasswordET);
        createAccountButton = findViewById(R.id.createAccountButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        //Checks for valid Username and Password entries and creates an account
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newUsernameET.getText().toString();
                String newPassword = newPasswordET.getText().toString();

                if(!newUsername.equals("") && !newPassword.equals("")) {
                    try {
                        String selection = "username=?";
                        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{newUsername}, null);
                        int count = cursor.getCount();
                        cursor.close();
                        Toast.makeText(getApplicationContext(), "-- \'" + count + "\'!", Toast.LENGTH_LONG).show();

                        if(count == 0){
                            User user = new User(newUsername);
                            String encryptedPassword = getEncodedString(newPassword);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(RecipeBookContentProvider.COLUMN_USERNAME, user.getUsername());
                            contentValues.put(RecipeBookContentProvider.COLUMN_PASSWORD, encryptedPassword);
                            contentValues.put(RecipeBookContentProvider.COLUMN_SAVEDRECIPES, user.getSavedRecipes());
                            contentValues.put(RecipeBookContentProvider.COLUMN_GROCERYLISTRECIPES,user.getGroceryListRecipes());
                        if (getApplicationContext().getContentResolver().insert(RecipeBookContentProvider.CONTENT_URI_U, contentValues) != null) {
                            Toast.makeText(getApplicationContext(), "Created account for \'" + newUsername + "\'!\nWelcome to the Recipe Book!", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Error creating account for \'" + newUsername + "\'!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error creating account for--- \'" + newUsername + "\'!", Toast.LENGTH_LONG).show();
                      }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please make sure both the Username and Password fields have been completed!", Toast.LENGTH_LONG).show();
            }
        });


        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //Encodes the password
    private static String getEncodedString(String s){
        return Base64.getEncoder().encodeToString(s.getBytes());
    }



}