package com.example.cw3;

import android.net.Uri;

//contract creation
//this is imported by classes who wish to use the content provider
//contains variables and column names of the URIs
public class ContractRecipe {

    public static final String AUTHORITY = "com.example.cw3.Provider";

    public static final Uri RECIPEURI = Uri.parse("content://" + AUTHORITY+ "/recipes");
    public static final Uri RECIPEIDURI = Uri.parse("content://" + AUTHORITY + "/recipes/_id");
    public static final Uri INGREDIENTSIDURI = Uri.parse("content://" + AUTHORITY + "/ingredients/_id");
    public static final Uri INGREDIENTSURI = Uri.parse("content://" + AUTHORITY+ "/ingredients");
    public static final Uri RECIPEINGREDIENTSURI = Uri.parse("content://" + AUTHORITY+ "/recipe_ingredients/recipe_id");
    public static final Uri RECIPEINGREDIENTSTABLEURI = Uri.parse("content://" + AUTHORITY+ "/recipe_ingredients"); //returns entire table

    public static final String RECIPEINGREDIENTID = "recipeingredientid";
    public static final String RECIPE_INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String RECIPE_INGREDIENT_INGREDIENT_ID = "ingredient_id";

    public static final String RECIPEID = "_id";
    public static final String RECIPENAME = "name";
    public static final String INSTRUCTION = "instructions";
    public static final String RATING = "rating";

    public static final String INGREDIENTID = "_id";
    public static final String INGREDIENTS = "ingredientname";





}
