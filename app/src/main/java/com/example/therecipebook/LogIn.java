package com.example.therecipebook;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {


    private EditText usernameET;
    private EditText passwordET;
    private Button loginButton;
    private TextView createUserTV;

    //Login Screen- was MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        createUserTV = findViewById(R.id.createUserTV);
        loginButton = findViewById(R.id.logInButton);

        //Developer purposes
        //Button duser = findViewById(R.id.dUserB);
        //Button drecipe = findViewById(R.id.dRecipeB);
        /*duser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplication().getContentResolver().delete(RecipeBookContentProvider.CONTENT_URI_U, null, null);
                Intent intent = new Intent(v.getContext(), LogIn.class);
                startActivity(intent);
            }
        });
        drecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplication().getContentResolver().delete(RecipeBookContentProvider.CONTENT_URI_R, null, null);
                Intent intent = new Intent(v.getContext(), LogIn.class);
                startActivity(intent);
            }
        });*/


        createUserTV.setPaintFlags(createUserTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        createUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateUser.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String userPasswordEntry = passwordET.getText().toString();
                if(!username.equals("") && !userPasswordEntry.equals("")) {
                    try {
                        String selection = "username=?";
                        Cursor cursor = getApplication().getContentResolver().query(RecipeBookContentProvider.CONTENT_URI_U, null, selection, new String[]{username}, null);
                        cursor.moveToNext();
                        String encryptedPassword = cursor.getString(2);     //The encrypted text stored in the database, we need to encrypt what the user entered
                        cursor.close();
                        //and check if these two strings match each other
                        if (encryptedPassword.equals(getEncodedString(userPasswordEntry))) {
                            Bundle bundle = new Bundle();
                            bundle.putString("user", username);
                            Intent intent = new Intent(v.getContext(), UserProfile.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else
                            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please make sure both the Username and Password fields have been completed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static String getEncodedString(String s){
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

}