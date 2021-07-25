package com.example.therecipebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class RecipeBookContentProvider extends ContentProvider {

    public static final String DBNAME = "RecipeBookDB";

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_USER);
            db.execSQL(SQL_CREATE_RECIPE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2){}

    };

    public final static int USER = 1;
    public final static int RECIPE = 2;

    public final static String TABLE_USERS= "Users";
    public final static String COLUMN_USERNAME = "username";
    public final static String COLUMN_PASSWORD = "password";
    public final static String COLUMN_SAVEDRECIPES = "savedRecipes";
    public final static String COLUMN_GROCERYLISTRECIPES = "groceryListRecipes";
    public final static String COLUMN_INBOXMESSAGES = "inboxMessages";


    public final static String TABLE_RECIPES= "Recipes";
    public final static String COLUMN_FORMAT = "format";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_CUISINE = "cuisine";
    public final static String COLUMN_COOKTIME = "cookTime";
    public final static String COLUMN_INGREDIENTS = "ingredients";
    public final static String COLUMN_INSTRUCTIONS = "instructions";
    public final static String COLUMN_URL = "url";
    public final static String COLUMN_NOTES = "notes";


    //Authority
    public static final String AUTHORITY = "com.example.recipe";
    public static final Uri CONTENT_URI_U = Uri.parse("content://" + AUTHORITY + "/" + TABLE_USERS);
    public static final Uri CONTENT_URI_R = Uri.parse("content://" + AUTHORITY + "/" + TABLE_RECIPES);

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_USERS, USER);
        uriMatcher.addURI(AUTHORITY, TABLE_RECIPES, RECIPE);
    }



    public static final String SQL_CREATE_USER = "CREATE TABLE " + TABLE_USERS +
            "(" + "_ID INTEGER PRIMARY KEY, " +
            COLUMN_USERNAME + " TEXT," +
            COLUMN_PASSWORD + " TEXT," +
            COLUMN_SAVEDRECIPES + " BLOB," +
            COLUMN_GROCERYLISTRECIPES+ " BLOB," +
            COLUMN_INBOXMESSAGES + " BLOB)";


    public static final String SQL_CREATE_RECIPE = "CREATE TABLE " + TABLE_RECIPES +
            "(" + "_ID INTEGER PRIMARY KEY, " +
            COLUMN_FORMAT + " TEXT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_CUISINE+ " TEXT," +
            COLUMN_COOKTIME + " INTEGER," +
            COLUMN_INGREDIENTS + " BLOB," +
            COLUMN_INSTRUCTIONS + " BLOB," +
            COLUMN_URL + " TEXT," +
            COLUMN_NOTES + " BLOB)";


    private MainDatabaseHelper openHelper;


    public RecipeBookContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        switch (uriMatcher.match(uri)){
            case USER:
                String username = values.getAsString(COLUMN_USERNAME).trim();
                String password = values.getAsString(COLUMN_PASSWORD).trim();

                        //This part will only become relevant if some form of error checking is needed,
                        //Since we are initializing User Profiles with empty lists, the values are initially
                        //set to null, and don't need to make sure they aren't for any reason
                        //byte[] savedRecipes = values.getAsByteArray(COLUMN_SAVEDRECIPES);
                        //byte[] groceryListRecipes = values.getAsByteArray(COLUMN_GROCERYLISTRECIPES);
                        //byte[] inboxMessages = values.getAsByteArray(COLUMN_INBOXMESSAGES);

                if(username.equals("")){ return null; }
                if(password.equals("")){ return null; }

                long id = openHelper.getWritableDatabase().insert(TABLE_USERS, null, values);
                return Uri.withAppendedPath(CONTENT_URI_U, "" + id);


            case RECIPE:
                String format = values.getAsString(COLUMN_FORMAT).trim();
                String name = values.getAsString(COLUMN_NAME).trim();
                String description = values.getAsString(COLUMN_DESCRIPTION).trim();
                String cuisine = values.getAsString(COLUMN_CUISINE).trim();
                String cookTime = values.getAsString(COLUMN_COOKTIME).trim();
                String ingredients = values.getAsString(COLUMN_INGREDIENTS).trim();
                String instructions = values.getAsString(COLUMN_INSTRUCTIONS).trim();
                String url = values.getAsString(COLUMN_URL).trim();
                String notes = values.getAsString(COLUMN_NOTES).trim();

                if(format.equals("")){ return null; }
                if(name.equals("")){ return null; }
                if(description.equals("")){ return null; }
                if(cuisine.equals("")){ return null; }
                if(cookTime.equals("")){ return null; }
                if(ingredients.equals("")){ return null; }
                if(instructions.equals("")){ return null; }
                if(url.equals("")){ return null; }
                if(notes.equals("")){ return null; }
                id = openHelper.getWritableDatabase().insert(TABLE_RECIPES, null, values);
                return Uri.withAppendedPath(CONTENT_URI_R, "" + id);

            default:
                return null;

               }
    }


    @Override
    public boolean onCreate() {
        openHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Uri _uri = null;
        switch (uriMatcher.match(uri)) {
            case USER:
                return openHelper.getReadableDatabase().query(TABLE_USERS, projection, selection, selectionArgs, null, null, sortOrder);
            case RECIPE:
                return openHelper.getReadableDatabase().query(TABLE_RECIPES, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }







}