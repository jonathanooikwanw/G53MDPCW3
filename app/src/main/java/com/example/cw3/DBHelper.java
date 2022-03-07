package com.example.cw3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    //creating the tables for the database - recipes/ingredients/recipe_ingredients
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE recipes ("+
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(128) NOT NULL," +
                "instructions VARCHAR(128) NOT NULL," +
                "rating REAL" + //float
        ");");

        db.execSQL("CREATE TABLE ingredients ("+
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "ingredientname VARCHAR(128) NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE recipe_ingredients ("+
                "recipe_id INT NOT NULL," +
                "ingredient_id INT NOT NULL," +
                "CONSTRAINT fk1 FOREIGN KEY (recipe_id) REFERENCES recipes (_id)," +
                "CONSTRAINT fk2 FOREIGN KEY (ingredient_id) REFERENCES ingredients (_id)," +
                "CONSTRAINT _id PRIMARY KEY (recipe_id, ingredient_id)" +
                ");");

    }

    //onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);

    }
}
