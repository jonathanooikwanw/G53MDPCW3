package com.example.cw3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    SimpleCursorAdapter dataAdapter;
    ListView listView;

    String recipe_ID;
    TextView recipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        queryRecipes();
        listView.setAdapter(dataAdapter);

        final ListView lv = (ListView) findViewById((R.id.listView)); //this displays all the recipes we have currently
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recipeView = view.findViewById(R.id.recipeID);
                recipe_ID = recipeView.getText().toString();
                Log.d("test", recipe_ID);
                Bundle recipeBundle = new Bundle(); //puts the recipeid in a bundle and sends it to the single recipe activity
                recipeBundle.putString("recipeID", recipe_ID);
                Intent recipeIntent = new Intent(MainActivity.this, SingleRecipe.class);
                recipeIntent.putExtras(recipeBundle);
                startActivity(recipeIntent);


            }
        });

    }

    public void onClickNew(View v) { //if user would like to create a new recipe

        Intent intent = new Intent(MainActivity.this, SingleRecipeAdd.class);
        startActivity(intent);
    }

    public void onClickIngredients(View v) //if user wants to view all the ingredients
    {
        Intent intent = new Intent(MainActivity.this, IngredientDisplay.class);
        startActivity(intent);
    }
    public void onClickSortTitle(View v) { //if user wants to sort by title


        String[] projectionRecipe = new String[]{ //makes projection of the recipe - projection represents the column in the table
                ContractRecipe.RECIPEID,
                ContractRecipe.RECIPENAME,
                ContractRecipe.RATING,
                ContractRecipe.INSTRUCTION
        };

        Cursor cursor = getContentResolver().query(ContractRecipe.RECIPEURI, projectionRecipe, null, null, "name ASC"); //change sortorder to sort by name
        dataAdapter.changeCursor(cursor);
        listView.setAdapter(dataAdapter); //changes the list view to sort by name
    }

    public void onClickSortRating(View v) { //if user wants to sort by rating


        String[] projectionRecipe = new String[]{
                ContractRecipe.RECIPEID,
                ContractRecipe.RECIPENAME,
                ContractRecipe.RATING,
                ContractRecipe.INSTRUCTION


        };

        Cursor cursor = getContentResolver().query(ContractRecipe.RECIPEURI, projectionRecipe, null, null, "rating DESC"); //change sort order to sort the rating from highest to lowest
        dataAdapter.changeCursor(cursor);
        listView.setAdapter(dataAdapter); //change the list view to sort by rating
    }

    public void queryRecipes() { //query the recipe database
        String[] recipeColumns = new String[]{
                ContractRecipe.RECIPEID,
                ContractRecipe.RECIPENAME,
                ContractRecipe.RATING,
                ContractRecipe.INSTRUCTION
        };

        int[] colResIdsRecipe = new int[]{
                R.id.recipeID,
                R.id.recipeName,
                R.id.recipeRating

        };

        //query the content provider to retrieve records
        Cursor cursor = getContentResolver().query(ContractRecipe.RECIPEURI, recipeColumns, null, null, null);

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.databaseitems,
                cursor,
                recipeColumns,
                colResIdsRecipe,
                0);

    }
}

