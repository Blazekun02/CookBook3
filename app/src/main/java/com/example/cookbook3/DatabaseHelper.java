package com.example.cookbook3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String dbName = "cookBook";
    private static final String catTable = "categories";
    private static final String recTable = "recipes";
    private static final String ingTable = "ingredients";
    private static final String recIngTable = "recipeIngredients";
    private static final String recPrepTable = "recipePreparations";
    private static final String shopListTable = "shoppingList";
    private static final String catCol1 = "categoryID";
    private static final String catCol2 = "categoryName";
    private static final String recCol1 = "recipeID";
    private static final String recCol2 = "recipeName";
    private static final String recCol3 = "description";
    private static final String recCol4 = "categoryID";
    private static final String ingCol1 = "ingredientID";
    private static final String ingCol2 = "ingredientName";
    private static final String recIngCol1 = "recipe_IngredientsID";
    private static final String recIngCol2 = "recipeID";
    private static final String recIngCol3 = "ingredientID";
    private static final String recIngCol4 = "quantity";
    private static final String recIngCol5 = "unit";
    private static final String recPrepCol1 = "recipe_PreparationsID";
    private static final String recPrepCol2 = "recipeID";
    private static final String recPrepCol3 = "stepNumber";
    private static final String recPrepCol4 = "description";
    private static final String shopListCol1 = "shoppingListID";
    private static final String shopListCol2 = "recipeID";
    private static final String shopListCol3 = "ingredientID";
    private static final String shopListCol4 = "quantity";
    private static final String shopListCol5 = "unit";
    private static final String shopListCol6 = "purchase";

    public DatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, 1);
        this.context = context;
    }

    // Create the database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + catTable + "(" +
                        catCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        catCol2 + " TEXT NOT NULL);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + recTable + "(" +
                        recCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        recCol2 + " TEXT NOT NULL, " +
                        recCol3 + " TEXT NOT NULL, " +
                        recCol4 + " INTEGER NOT NULL, " + "FOREIGN KEY (" + recCol4 + ") REFERENCES " + catTable + "(" + catCol1 + "));"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ingTable + "(" +
                        ingCol1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ingCol2 + " TEXT NOT NULL);"
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

    //upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + catTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + recTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ingTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + recIngTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + recPrepTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + shopListTable);
        onCreate(sqLiteDatabase);
    }

    void addCategory(String catName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(catCol2, catName);
        long result = db.insert(catTable, null, cv);
        if (result == -1) {
            Toast.makeText(null, "Failed to add category", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(null, "Category added successfully", Toast.LENGTH_SHORT).show();
        }

    }

    void addRecipe(String recName, String recDesc, int catID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(recCol2, recName);
        cv.put(recCol3, recDesc);
        cv.put(recCol4, catID);
    }

    void addIngredient(String ingName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + ingTable + " (" + ingCol2 + ") VALUES ('" + ingName + "');");
    }

    void addRecipeIngredient(int recID, int ingID, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + recIngTable + " (" + recIngCol2 + ", " + recIngCol3 + ", " + recIngCol4 + ", " + recIngCol5 + ") VALUES (" + recID + ", " + ingID + ", " + quantity + ", '" + unit + "');");
    }

    void addRecipePreparation(int recID, int stepNum, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + recPrepTable + " (" + recPrepCol2 + ", " + recPrepCol3 + ", " + recPrepCol4 + ") VALUES (" + recID + ", " + stepNum + ", '" + desc + "');");
    }

    void addShoppingListItem(int recID, int ingID, double quantity, String unit, int purchase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + shopListTable + " (" + shopListCol2 + ", " + shopListCol3 + ", " + shopListCol4 + ", " + shopListCol5 + ", " + shopListCol6 + ") VALUES (" + recID + ", " + ingID + ", " + quantity + ", '" + unit + "', " + purchase + ");");
    }
}
