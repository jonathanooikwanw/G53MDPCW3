package com.example.cw3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class IngredientDisplay extends AppCompatActivity {

    SimpleCursorAdapter dataAdapter;
    ListView ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_display);
        ingredientList = findViewById(R.id.ingredientList);
        queryIngredients();
        ingredientList.setAdapter(dataAdapter);
    }

    public void queryIngredients() { //query the ingredient database and displays in a list view
        String[] ingredientColumns = new String[]{ //tells the query method what columns to return
                ContractRecipe.INGREDIENTID,
                ContractRecipe.INGREDIENTS
        };

        int[] colResIdsRecipe = new int[]{ //displays in the text view
                R.id.ingredientID,
                R.id.ingredientName

        };

        //query the content provider to retrieve records
        Cursor cursor = getContentResolver().query(ContractRecipe.INGREDIENTSURI, ingredientColumns, null, null, null);

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.ingredients,
                cursor,
                ingredientColumns,
                colResIdsRecipe,
                0);

    }
}

