package com.example.cookbook3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

        returnBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(RecipeListPage.this, CategoryPage.class);
            startActivity(intent1);
        });

        addRecipe.setOnClickListener(v -> showAddRecipeDialog());

    }

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Recipe");

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
            String recipeNameStr = recipeName.getText().toString();
            String descriptionStr = description.getText().toString();
            Integer categoryID = getIntent().getIntExtra("categoryID", 0);

            if(recipeNameStr.isEmpty() || descriptionStr.isEmpty()) {
                Toast.makeText(RecipeListPage.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                myDB.addRecipe(recipeNameStr, descriptionStr, categoryID);
                Toast.makeText(RecipeListPage.this, "Recipe added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}