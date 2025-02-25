package com.example.cookbook3;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeListPage extends AppCompatActivity {
    ImageButton returnBtn;
    Button addRecipe;
    DatabaseHelper myDB;
    int selectedCategoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_list_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        returnBtn = findViewById(R.id.returntocategoryBtns);
        addRecipe = findViewById(R.id.addRecipeBtn);

        myDB = new DatabaseHelper(RecipeListPage.this);

        Intent intent = getIntent();
        selectedCategoryID = intent.getIntExtra("selectedCategoryID", -1);
        if (selectedCategoryID == -1) {
            Toast.makeText(this, "Error: No category selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        returnBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(RecipeListPage.this, CategoryPage.class);
            startActivity(intent1);
            finish();
        });

        addRecipe.setOnClickListener(v -> showAddRecipeDialog());

        displayRecipes();

    }

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Recipe");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        EditText recipeName = new EditText(this);
        recipeName.setHint("Recipe Name");
        layout.addView(recipeName);

        EditText description = new EditText(this);
        description.setHint("Description");
        layout.addView(description);



        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String recipeNameStr = recipeName.getText().toString().trim();
            String descriptionStr = description.getText().toString().trim();

            if (recipeNameStr.isEmpty() || descriptionStr.isEmpty()) {
                Toast.makeText(RecipeListPage.this, "Please enter a recipe name and description", Toast.LENGTH_SHORT).show();
            } else {
                myDB.addRecipe(recipeNameStr, descriptionStr, selectedCategoryID);
                Toast.makeText(RecipeListPage.this, "Recipe added", Toast.LENGTH_SHORT).show();
                displayRecipes();
            }
        });

        builder.create().show();
    }

    private void displayRecipes() {
        Cursor cursor = myDB.getRecipes(selectedCategoryID); // Fetch only recipes in this category
        LinearLayout recipeContainer = findViewById(R.id.recipesContainer);
        recipeContainer.removeAllViews();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No recipes found in this category", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int recipeID = cursor.getInt(0);
                String recipeName = cursor.getString(1);
                String description = cursor.getString(2);
                Button recipeButton = new Button(this);
                recipeButton.setText(recipeName);
                recipeButton.setPadding(20, 10, 20, 10);
                recipeButton.setBackgroundColor(Color.parseColor("#FF9800"));
                recipeButton.setTextColor(Color.WHITE);

                recipeButton.setOnLongClickListener(v -> {
                    showRecipeOptions(v,recipeID, recipeName, description);
                    return true;
                });

                recipeButton.setOnClickListener(v -> {
                    Toast.makeText(this, "Recipe clicked: " + recipeName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecipeListPage.this, ViewRecipePage.class);
                    intent.putExtra("RECIPE_ID", recipeID);
                    startActivity(intent);
                });

                recipeContainer.addView(recipeButton);
            }
        }
        cursor.close();
    }

    private void showRecipeOptions(View view, int recipeID, String recipeName, String description) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Edit");
        popupMenu.getMenu().add("Delete");

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Edit")) {
                showEditRecipeDialog(recipeID, recipeName, description);
            } else if (menuItem.getTitle().equals("Delete")) {
                showDeleteRecipeDialog(recipeID, recipeName);
            }
            return true;
        });

        popupMenu.show();
    }

    private void showEditRecipeDialog(int recipeID, String oldName, String oldDescription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Recipe");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        EditText recipeName = new EditText(this);
        recipeName.setText(oldName);
        layout.addView(recipeName);

        EditText description = new EditText(this);
        description.setText(oldDescription);
        layout.addView(description);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = recipeName.getText().toString().trim();
            String newDesc = description.getText().toString().trim();

            if (newName.isEmpty() || newDesc.isEmpty()) {
                Toast.makeText(this, "Please enter a recipe name and description", Toast.LENGTH_SHORT).show();
            } else {
                myDB.updateRecipe(recipeID, newName, newDesc);
                Toast.makeText(this, "Recipe updated!", Toast.LENGTH_SHORT).show();
                displayRecipes();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showDeleteRecipeDialog(int recipeID, String recipeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Recipe");
        builder.setMessage("Are you sure you want to delete \"" + recipeName + "\"?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            myDB.deleteRecipe(recipeID);
            Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();
            displayRecipes();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

}