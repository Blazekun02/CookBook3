package com.example.cookbook3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewRecipePage extends AppCompatActivity {

    DatabaseHelper myDB;
    ImageButton returnBtn;
    TextView tvRecipeName, tvRecipeDescription;
    ListView listViewIngredients, listViewSteps;
    Button editViewRecipeBtn;
    int recipeID;
    ArrayList<String> ingredientsList = new ArrayList<>();
    ArrayList<String> stepsList = new ArrayList<>();
    ArrayAdapter<String> ingredientsAdapter, stepsAdapter;


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

        tvRecipeName = findViewById(R.id.recipeDisplay);
        tvRecipeDescription = findViewById(R.id.descDisplay);
        listViewIngredients = findViewById(R.id.listViewIngredients);
        listViewSteps = findViewById(R.id.listViewSteps);
        returnBtn = findViewById(R.id.returnToRecipeBtn);
        editViewRecipeBtn = findViewById(R.id.editViewRecipeBtn);

        Intent intent = getIntent();
        recipeID = intent.getIntExtra("RECIPE_ID", -1);

        if (recipeID != -1) {
            loadRecipeDetails();
            loadIngredients();
        }

        returnBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(ViewRecipePage.this, RecipeListPage.class);
            startActivity(intent1);
            finish();
        });
        // Load Recipe Data
        loadRecipeDetails();
        loadIngredients();
        loadSteps();

        // Set Up Adapters for ListView
        ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredientsList);
        listViewIngredients.setAdapter(ingredientsAdapter);

        stepsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stepsList);
        listViewSteps.setAdapter(stepsAdapter);

        // Use editViewRecipeBtn for Adding Items
        editViewRecipeBtn.setOnClickListener(v -> showAddItemDialog());

    }

    // Load Recipe Details from Database
    private void loadRecipeDetails() {
        Cursor cursor = myDB.getRecipeById(recipeID);
        if (cursor.moveToFirst()) {
            tvRecipeName.setText(cursor.getString(1)); // Recipe Name
            tvRecipeDescription.setText(cursor.getString(2)); // Recipe Description
        }
        cursor.close();
    }

    // Load Ingredients from Database
    private void loadIngredients() {
        ingredientsList.clear();
        Cursor cursor = myDB.getIngredientsByRecipeId(recipeID);
        while (cursor.moveToNext()) {
            ingredientsList.add(cursor.getString(1)); // Ingredient Name
        }
        cursor.close();
    }

    // Load Steps from Database
    private void loadSteps() {
        stepsList.clear();
        Cursor cursor = myDB.getStepsByRecipeId(recipeID);
        while (cursor.moveToNext()) {
            stepsList.add(cursor.getString(1)); // Step Description
        }
        cursor.close();
    }

    // Show Dialog to Choose Between Adding an Ingredient or a Step
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What do you want to add?");

        // Options to choose
        String[] options = {"Ingredient", "Preparation Step"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showAddIngredientDialog(); // User chose Ingredient
            } else {
                showAddStepDialog(); // User chose Step
            }
        });

        builder.show();
    }

    // Show Dialog to Add Ingredient
    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Ingredient");

        // Input Field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter ingredient...");
        builder.setView(input);

        // Buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String ingredient = input.getText().toString().trim();
            if (!ingredient.isEmpty()) {
                myDB.addIngredient(recipeID, ingredient); // Add to DB
                ingredientsList.add(ingredient);
                ingredientsAdapter.notifyDataSetChanged(); // Update ListView
                Toast.makeText(this, "Ingredient Added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Show Dialog to Add Step
    private void showAddStepDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Preparation Step");

        // Input Field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter step...");
        builder.setView(input);

        // Buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String step = input.getText().toString().trim();
            if (!step.isEmpty()) {
                myDB.addStep(recipeID, step); // Add to DB
                stepsList.add(step);
                stepsAdapter.notifyDataSetChanged(); // Update ListView
                Toast.makeText(this, "Step Added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}