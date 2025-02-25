package com.example.cookbook3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String dbName = "cookBook.db";
    private static final int dbVersion = 1;

    // Table Names
    private static final String catTable = "categories";
    private static final String recTable = "recipes";
    private static final String ingTable = "ingredients";
    private static final String recIngTable = "recipeIngredients";
    private static final String recPrepTable = "recipePreparations";
    private static final String shopListTable = "shoppingList";

    // Categories Table Columns
    private static final String catCol1 = "categoryID";
    private static final String catCol2 = "categoryName";

    // Recipes Table Columns
    private static final String recCol1 = "recipeID";
    private static final String recCol2 = "recipeName";
    private static final String recCol3 = "description";
    private static final String recCol4 = "categoryID";

    // Ingredients Table Columns
    private static final String ingCol1 = "ingredientID";
    private static final String ingCol2 = "ingredientName";

    // Recipe Ingredients Table Columns
    private static final String recIngCol1 = "recipe_IngredientsID";
    private static final String recIngCol2 = "recipeID";
    private static final String recIngCol3 = "ingredientID";
    private static final String recIngCol4 = "quantity";
    private static final String recIngCol5 = "unit";

    // Recipe Preparations Table Columns
    private static final String recPrepCol1 = "recipe_PreparationsID";
    private static final String recPrepCol2 = "recipeID";
    private static final String recPrepCol3 = "stepNumber";
    private static final String recPrepCol4 = "description";

    public DatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + catTable + "(" +
                catCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                catCol2 + " TEXT NOT NULL UNIQUE);"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + recTable + "(" +
                recCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                recCol2 + " TEXT NOT NULL, " +
                recCol3 + " TEXT NOT NULL, " +
                recCol4 + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + recCol4 + ") REFERENCES " + catTable + "(" + catCol1 + "));"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ingTable + "(" +
                ingCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ingCol2 + " TEXT NOT NULL UNIQUE);"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + recIngTable + "(" +
                recIngCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                recIngCol2 + " INTEGER NOT NULL, " +
                recIngCol3 + " INTEGER NOT NULL, " +
                recIngCol4 + " REAL NOT NULL, " +
                recIngCol5 + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + recIngCol2 + ") REFERENCES " + recTable + "(" + recCol1 + "), " +
                "FOREIGN KEY (" + recIngCol3 + ") REFERENCES " + ingTable + "(" + ingCol1 + "));"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + recPrepTable + "(" +
                recPrepCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                recPrepCol2 + " INTEGER NOT NULL, " +
                recPrepCol3 + " INTEGER NOT NULL, " +
                recPrepCol4 + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + recPrepCol2 + ") REFERENCES " + recTable + "(" + recCol1 + "));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + catTable);
        db.execSQL("DROP TABLE IF EXISTS " + recTable);
        db.execSQL("DROP TABLE IF EXISTS " + ingTable);
        db.execSQL("DROP TABLE IF EXISTS " + recIngTable);
        db.execSQL("DROP TABLE IF EXISTS " + recPrepTable);
        db.execSQL("DROP TABLE IF EXISTS " + shopListTable);
        onCreate(db);
    }

    public void addCategory(String catName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(catCol2, catName);
        db.insert(catTable, null, cv);
        db.close();
    }

    public void addRecipe(String recipeName, String description, Integer categoryID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(recCol2, recipeName);
        cv.put(recCol3, description);
        cv.put(recCol4, categoryID);
        db.insert(recTable, null, cv);
        db.close();
    }

    public void updateCategory(String categoryId, String newCategoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(catCol2, newCategoryName);
        db.update(catTable, contentValues, catCol1 + " = ?", new String[]{categoryId});
        db.close();
    }

    public void deleteCategory(String categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(catTable, catCol1 + " = ?", new String[]{categoryId});
        db.close();
    }

    public Cursor readCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + catTable, null);
    }

    public Cursor getRecipes(int categoryID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + recTable + " WHERE " + recCol4 + " = ?", new String[]{String.valueOf(categoryID)});
    }

    public void updateRecipe(int recipeID, String newName, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(recCol2, newName);
        cv.put(recCol3, newDescription);

        db.update(recTable, cv, recCol1 + " = ?", new String[]{String.valueOf(recipeID)});
        db.close();
    }

    public void deleteRecipe(int recipeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(recTable, recCol1 + " = ?", new String[]{String.valueOf(recipeID)});
        db.close();
    }

    public void addIngredient(int recipeID, String ingredientName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if ingredient already exists
        Cursor cursor = db.rawQuery("SELECT " + ingCol1 + " FROM " + ingTable + " WHERE " + ingCol2 + " = ?", new String[]{ingredientName});
        int ingredientID;

        if (cursor.moveToFirst()) {
            ingredientID = cursor.getInt(0); // Get existing ingredient ID
        } else {
            // Insert new ingredient
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put(ingCol2, ingredientName);
            ingredientID = (int) db.insert(ingTable, null, ingredientValues);
        }
        cursor.close();

        // Now insert into recipeIngredients table
        ContentValues recIngValues = new ContentValues();
        recIngValues.put(recIngCol2, recipeID);
        recIngValues.put(recIngCol3, ingredientID);
        recIngValues.put(recIngCol4, 1); // Default quantity = 1 (can be changed)
        recIngValues.put(recIngCol5, "unit"); // Default unit (modify as needed)

        db.insert(recIngTable, null, recIngValues);
        db.close();
    }

    // Add a preparation step to the database (for a specific recipe)
    public void addStep(int recipeID, String stepDescription) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Find the current highest step number
        Cursor cursor = db.rawQuery("SELECT MAX(" + recPrepCol3 + ") FROM " + recPrepTable + " WHERE " + recPrepCol2 + " = ?", new String[]{String.valueOf(recipeID)});
        int stepNumber = 1;

        if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
            stepNumber = cursor.getInt(0) + 1; // Get next step number
        }
        cursor.close();

        // Insert step into recipePreparations table
        ContentValues prepValues = new ContentValues();
        prepValues.put(recPrepCol2, recipeID);
        prepValues.put(recPrepCol3, stepNumber);
        prepValues.put(recPrepCol4, stepDescription);

        db.insert(recPrepTable, null, prepValues);
        db.close();
    }

    // Get all ingredients for a given recipe
    public Cursor getIngredientsByRecipeId(int recipeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT i." + ingCol2 + " FROM " + ingTable + " i " +
                        "JOIN " + recIngTable + " ri ON i." + ingCol1 + " = ri." + recIngCol3 +
                        " WHERE ri." + recIngCol2 + " = ?",
                new String[]{String.valueOf(recipeID)}
        );
    }

    // Get all preparation steps for a given recipe
    public Cursor getStepsByRecipeId(int recipeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT " + recPrepCol4 + " FROM " + recPrepTable +
                        " WHERE " + recPrepCol2 + " = ?" +
                        " ORDER BY " + recPrepCol3 + " ASC",
                new String[]{String.valueOf(recipeID)}
        );
    }

    // Retrieve a single recipe by its ID
    public Cursor getRecipeById(int recipeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + recTable + " WHERE " + recCol1 + " = ?",
                new String[]{String.valueOf(recipeID)}
        );
    }
}
