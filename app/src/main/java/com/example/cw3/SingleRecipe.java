package com.example.cw3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class SingleRecipe extends AppCompatActivity {

    TextView recipeName, recipeInstruction, recipeIngredients;
    String recipeID, name, instructions;
    int id;
    float rating;
    RatingBar ratingBar;
    private DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);
        recipeName = findViewById(R.id.recipeName);
        recipeIngredients = findViewById(R.id.recipeIngredient);
        recipeInstruction = findViewById(R.id.recipeInstruction);
        ratingBar = findViewById(R.id.ratingBar);

        Bundle bundle = getIntent().getExtras();
        recipeID = bundle.getString("recipeID"); //used for the cursor
        obtainRecipe();
        queryIngredients();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() { //if user changes the rating of a recipe
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float ratingUpdate = ratingBar.getRating();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ContractRecipe.RATING, ratingUpdate); //puts the updated rating in the content values
                getContentResolver().update(ContractRecipe.RECIPEURI, contentValues, recipeID, null); //updates the database

                Intent intent = new Intent(SingleRecipe.this,MainActivity.class); //sends the user back to the main activity for viewing
                startActivity(intent);
            }
        });

    }

    public void obtainRecipe() { //gets the recipe that the user has selected on the main activity

        String[] projectionRecipe = new String[]{ //tells the query method what columns to return
                ContractRecipe.RECIPEID,
                ContractRecipe.RECIPENAME,
                ContractRecipe.INSTRUCTION,
                ContractRecipe.RATING
        };

        //querys the recipe database using the id and gets all the columns associated to the id
        Cursor cursor = getContentResolver().query(ContractRecipe.RECIPEIDURI, projectionRecipe,recipeID,null,null);

        if(cursor.moveToFirst())
        {
            do {
                {
                    id = cursor.getInt(0); //returns the id of the recipe selected
                    name = cursor.getString(1); //returns the name of the recipe selected
                    instructions = cursor.getString(2 ); //returns the instructions of the recipe selected
                    rating = cursor.getFloat(3); //returns the rating of the recipe selected

                }
            } while
            (cursor.moveToNext());
        }
        recipeName.setText(name); //sets the text for the recipe name
        recipeInstruction.setText("Instructions : " + instructions); //sets the text for the recipe instruction
        ratingBar.setRating(rating); //sets the rating
    }

    public void queryIngredients() //query the ingredient database for the recipes

    {
        StringBuilder sb = new StringBuilder();
        String[] recipe_ID = new String[] {recipeID}; //puts the recipeid in a string array
        Cursor cursor = getContentResolver().query(ContractRecipe.RECIPEINGREDIENTSURI, null ,null ,recipe_ID,null); //querys the recipe_ingredient database using the recipe id to get the ingredients
        if(cursor.moveToFirst())
        {
            do {
                {
                    String ingredients = cursor.getString(3); //gets the ingredients - it is column 3 due to the ingredient id being the 3rd column in the recipe_ingredients table
                    sb.append(ingredients + "\n");
                }
            } while
            (cursor.moveToNext());
        }

        recipeIngredients.setText(sb); //sets the combined string in the textview
    }

    //if the user deletes a recipe
    public void onClickDelete(View v)
    {
        //check if recipe exists
        String[] recipe_ID = new String[] {recipeID};
        Cursor cursor = getContentResolver().query(ContractRecipe.RECIPEINGREDIENTSURI, null , null, recipe_ID, null);
        Log.d("cursor", "create");
        getContentResolver().delete(ContractRecipe.RECIPEURI, recipeID, null);
        getContentResolver().delete(ContractRecipe.RECIPEINGREDIENTSURI, recipeID, null);

        if(cursor.moveToFirst())

        {
            do {
                Cursor checkIng = getContentResolver().query(ContractRecipe.RECIPEINGREDIENTSTABLEURI, null, "ingredient_id =" + Integer.toString(cursor.getInt(2)), null ,null);
                    //check if ingredient is used by more than one recipe
                if(checkIng.moveToFirst()) //if the ingredient is used by another recipe
                {
                    Log.d("ingredient is being used", "cw3");
                }
                else //if the ingredient is only used by the recipe that is being deleted
                {
                    getContentResolver().delete(ContractRecipe.INGREDIENTSIDURI, Integer.toString(cursor.getInt(2)),null);
                }
            } while (cursor.moveToNext());
        }

        Intent intent = new Intent(SingleRecipe.this,MainActivity.class);
        startActivity(intent);
    }

}
