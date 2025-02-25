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

    // Shopping List Table Columns
    private static final String shopListCol1 = "shoppingListID";
    private static final String shopListCol2 = "recipeID";
    private static final String shopListCol3 = "ingredientID";
    private static final String shopListCol4 = "quantity";
    private static final String shopListCol5 = "unit";
    private static final String shopListCol6 = "purchase";

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

        db.execSQL("CREATE TABLE IF NOT EXISTS " + shopListTable + "(" +
                shopListCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shopListCol2 + " INTEGER NOT NULL, " +
                shopListCol3 + " INTEGER NOT NULL, " +
                shopListCol4 + " REAL NOT NULL, " +
                shopListCol5 + " TEXT NOT NULL, " +
                shopListCol6 + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + shopListCol2 + ") REFERENCES " + recTable + "(" + recCol1 + "), " +
                "FOREIGN KEY (" + shopListCol3 + ") REFERENCES " + ingTable + "(" + ingCol1 + "));"
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

    public Cursor compareCategory(String catName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + catTable + " WHERE " + catCol2 + " = ?", new String[]{catName});
    }
}
