package com.example.cw3;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;


public class Provider extends ContentProvider {


    private static final int RECIPE = 1;
    private static final int RECIPE_ID = 2;
    private static final int INGREDIENT = 3;
    private static final int INGREDIENT_ID = 4;
    private static final int RECIPE_INGREDIENT_RECIPE_ID = 5;
    private static final int RECIPE_INGREDIENT = 6;



    private DBHelper dbHelper = null;
    private static final UriMatcher uriMatcher;

    //UriMatcher to match Uri paths to the database
    //This is to select which database to use when inserting and querying data
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, "recipes", RECIPE);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, "recipes/_id", RECIPE_ID);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, "ingredients", INGREDIENT);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, "ingredients/_id", INGREDIENT_ID);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, "recipe_ingredients/recipe_id", RECIPE_INGREDIENT_RECIPE_ID);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, "recipe_ingredients", RECIPE_INGREDIENT);
    }

    @Override
    public boolean onCreate()
    {
        Log.d("asdf", "oncreate");
        this.dbHelper = new DBHelper(this.getContext(), "myDB" , null , 7);
        return true;
    }

    //determines the type of data that the user will get back
    @Override
    public String getType(Uri uri) {
        if (uri.getLastPathSegment()==null)
        {
            return "vnd.android.cursor.dir/Provider.data.text";
        }
        else
        {
            return "vnd.android.cursor.item/Provider.data.text";
        }
    }


    //Inserting data into the database
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        //selecting which table to use when inserting data
        switch(uriMatcher.match(uri))
        {
            case 1:
                tableName = "recipes";
                break;

            case 3:
                tableName = "ingredients";
                break;

            case 5:
                tableName = "recipe_ingredients";
                break;

            default:
                tableName = "recipes";
                break;
        }

        //inserts the data into the database
        long id = db.insert(tableName, null , values);
        db.close();

        //returns a new URI to identify the data that was inserted
        Uri newURI = ContentUris.withAppendedId(uri, id);

        Log.d("g69mdp", newURI.toString());
        getContext().getContentResolver().notifyChange(newURI, null);
        return newURI;
    }

    //querying the database for data
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri))
        {
            case 1:
                // this returns the entire recipe table
                return sqlDB.query("recipes", projection, selection , selectionArgs, null, null, sortOrder);

            case 2:
                //this returns only the recipe id column of the recipe table
                Log.d("please", "case 2");
                selection = "_ID = " + selection; //gets the id of the row
                return sqlDB.query("recipes", projection, selection , selectionArgs, null, null, sortOrder);

            case 3:
                return sqlDB.query("ingredients", projection, selection, selectionArgs,null, null, sortOrder);
            case 4:
                selection = "INGREDIENTNAME = ?" ; //a catchall for the ingredient name since selection didnt work for some reason
                return sqlDB.query("ingredients", projection, selection , selectionArgs, null, null, sortOrder);


            case 5:
                //extracts the data from the tables given the recipe id and retrieves the ingredients based on the recipe_ingredient association
                return sqlDB.rawQuery("select r._id as recipe_id, r.name, ri.ingredient_id, i.ingredientname "+
                        "from recipes r "+
                                "join recipe_ingredients ri on (r._id = ri.recipe_id)"+
                                "join ingredients i on (ri.ingredient_id = i._id) where r._id == ?",
                        selectionArgs);
            case 6:
                //returns entire ingredients table
                return sqlDB.query("recipe_ingredients", projection, selection, selectionArgs,null, null, sortOrder);

            default: return null;
}
    }

    //updates the data when the user enters a new recipe
@Override
    public int update(Uri uri, ContentValues values, String selection, String[]
            selectionArgs) {

    int id;
    SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
    id = sqlDB.update("recipes", values,"_id= " + selection ,null);

    return id;

}

//this is run when the user deletes a recipe
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int row;
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case 5:
                row = sqlDB.delete("recipes", "_id= " + selection ,null);
                row += sqlDB.delete("recipe_ingredients", "recipe_id= " + selection, null);
                return row;
            case 4:
                row = sqlDB.delete("ingredients", "_id= " + selection, null);
                return row;
                default: return 0;

        }



    }

}
