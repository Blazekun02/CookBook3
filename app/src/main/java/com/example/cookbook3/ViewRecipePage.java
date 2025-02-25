package com.example.cookbook3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewRecipePage extends AppCompatActivity {

    DatabaseHelper myDB;
    TextView tvRecipeName, tvRecipeDescription;
    ListView listViewIngredients, listViewSteps;
    int recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_recipe_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDB = new DatabaseHelper(this);

        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        listViewIngredients = findViewById(R.id.listViewIngredients);
        listViewSteps = findViewById(R.id.listViewSteps);

        Intent intent = getIntent();
        recipeID = intent.getIntExtra("RECIPE_ID", -1);

        if (recipeID != -1) {
            loadRecipeDetails();
            loadIngredients();
            loadPreparationSteps();
        }
    }

    private void loadRecipeDetails() {
        Cursor cursor = myDB.getRecipeDetails(recipeID);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("recipeName"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            tvRecipeName.setText(name);
            tvRecipeDescription.setText(description);
        }
        cursor.close();
    }

    private void loadIngredients() {
        Cursor cursor = myDB.getRecipeIngredients(recipeID);
        ArrayList<String> ingredientsList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String ingredient = cursor.getString(0);  // ingredientName
            double quantity = cursor.getDouble(1);    // quantity
            String unit = cursor.getString(2);        // unit
            ingredientsList.add(quantity + " " + unit + " - " + ingredient);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredientsList);
        listViewIngredients.setAdapter(adapter);
    }

    private void loadPreparationSteps() {
        Cursor cursor = myDB.getRecipeSteps(recipeID);
        ArrayList<String> stepsList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int stepNumber = cursor.getInt(0);
            String description = cursor.getString(1);
            stepsList.add("Step " + stepNumber + ": " + description);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stepsList);
        listViewSteps.setAdapter(adapter);
    }
}