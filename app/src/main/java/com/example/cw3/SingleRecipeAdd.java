package com.example.cw3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;


public class SingleRecipeAdd extends AppCompatActivity {

    EditText name, ingredients, instructions;
    RatingBar rating;
    String recipeName, recipeInstruction;
    String[] recipeIngredient;
    int[] addIngredient = new int[10];

    int ingredientCounter = 0;
    float recipeRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe_add);
        name = findViewById(R.id.insertTitle);
        ingredients = findViewById(R.id.insertIngredient);
        instructions = findViewById(R.id.insertInstruction);
        rating = findViewById(R.id.insertRating);


    }

    //after the user has filled out all the text boxes and clicks on the add recipe button
    public void onClickAdd(View v) {

        Log.d("added", "test");
        recipeName = name.getText().toString();
        recipeInstruction = instructions.getText().toString();
        recipeIngredient = ingredients.getText().toString().split("\\r?\\n"); //the ingredients are split using newline
        recipeRating = rating.getRating();

        //projection used for cursor
        String[] ingredientColumns = new String[]{ //tells the query method which columns to return
                ContractRecipe.INGREDIENTID,
                ContractRecipe.INGREDIENTS
        };



        for (String ingredient : recipeIngredient) //for every ingredient that was entered
        {
            String ingredients[] = new String[]{ingredient}; //put the ingredient into an array of size one - this is only done since we have to use selectionArgs instead of selection
            //it does not work if we pass the ingredient normally
            Cursor cursor = getContentResolver().query(ContractRecipe.INGREDIENTSIDURI, ingredientColumns, null, ingredients, null);//querys the ingredient database to get the ingredient

            if (cursor.moveToFirst()) //if ingredient exist in the table
            {
              addIngredient[ingredientCounter] = cursor.getInt(0);//get the id of the ingredient and add it to the array
                ingredientCounter++; //increments the ingredient counter
            }
            else
            {
                //if the ingredients does not exist in the table
                ContentValues ingredientValues = new ContentValues();
                ingredientValues.put(ContractRecipe.INGREDIENTS, ingredient); //add the ingredient
                //everytime we add something we get an URI and the last digit of the URI is the id of the thing we added
                //getLastPathSegment gets that number and adds it to the addIngredient array
                addIngredient[ingredientCounter] = Integer.parseInt(getContentResolver().insert(ContractRecipe.INGREDIENTSURI, ingredientValues).getLastPathSegment());
                ingredientCounter++; //increments for every ingredient we added

            }
        }

        ContentValues values = new ContentValues();
        values.put(ContractRecipe.RECIPENAME, recipeName); //puts the recipe name
        values.put(ContractRecipe.INSTRUCTION, recipeInstruction); //puts the recipe instruction
        values.put(ContractRecipe.RATING, recipeRating); //puts the recipe rating

        String recipeID = (getContentResolver().insert(ContractRecipe.RECIPEURI, values).getLastPathSegment()); //gets the id of the recipe we added in the table

        for ( int i = 0; addIngredient[i]!=0 ; i++) //for every ingredient we added
        {
            ContentValues newRecipeIngredient = new ContentValues();
            newRecipeIngredient.put(ContractRecipe.RECIPE_INGREDIENT_RECIPE_ID, recipeID); //puts the recipe id for that ingredient in the recipe_ingredients table
            newRecipeIngredient.put(ContractRecipe.RECIPE_INGREDIENT_INGREDIENT_ID, addIngredient[i]); //puts the ingredient id in the recipe_ingredient table
            getContentResolver().insert(ContractRecipe.RECIPEINGREDIENTSURI, newRecipeIngredient);
        }
        Intent intent = new Intent(SingleRecipeAdd.this, MainActivity.class);
        startActivity(intent);
    }
}

