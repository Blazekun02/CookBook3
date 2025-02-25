package com.example.cookbook3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CategoryPage extends AppCompatActivity {

    ImageButton returnBtn;
    Button addF;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDB = new DatabaseHelper(CategoryPage.this);

        returnBtn = findViewById(R.id.returnBtn);
        addF = findViewById(R.id.addBtn);

        returnBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(CategoryPage.this, MainActivity.class);
            startActivity(intent1);
        });

        addF.setOnClickListener(v -> showAddCategoryDialog());

        displayCategories();
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Category");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        final EditText inputCatName = new EditText(this);
        inputCatName.setHint("Enter Category name");
        layout.addView(inputCatName);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String categoryName = inputCatName.getText().toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Category name cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isCategoryAlreadyExists(categoryName)) {
                Toast.makeText(this, "Category already exists!", Toast.LENGTH_SHORT).show();
                return;
            }
            myDB.addCategory(categoryName);
            displayCategories();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private boolean isCategoryAlreadyExists(String categoryName) {
        Cursor cursor = myDB.readCategories();
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equalsIgnoreCase(categoryName)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    void displayCategories() {
        Cursor cursor = myDB.readCategories();
        LinearLayout categoryContainer = findViewById(R.id.categoryContainer);
        categoryContainer.removeAllViews();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to display", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String categoryId = cursor.getString(0);
                String categoryName = cursor.getString(1);

                Button categoryButton = new Button(this);
                categoryButton.setText(categoryName);
                categoryButton.setPadding(20, 10, 20, 10);
                categoryButton.setBackgroundColor(Color.parseColor("#FF9800"));
                categoryButton.setTextColor(Color.WHITE);
                categoryButton.setTextAlignment(Button.TEXT_ALIGNMENT_VIEW_START);

                categoryButton.setOnLongClickListener(v -> {
                    showCategoryOptions(v, categoryId, categoryName);
                    return true;
                });

                categoryButton.setOnClickListener(v -> {
                    Intent intent = new Intent(CategoryPage.this, RecipePage.class);
                    intent.putExtra("CATEGORY_NAME", categoryName);
                    startActivity(intent);
                });

                categoryContainer.addView(categoryButton);
            }
        }
        cursor.close();
    }

    private void showCategoryOptions(View view, String categoryId, String categoryName) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Edit");
        popupMenu.getMenu().add("Delete");

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Edit")) {
                showEditCategoryDialog(categoryId, categoryName);
            } else if (menuItem.getTitle().equals("Delete")) {
                showDeleteCategoryDialog(categoryId, categoryName);
            }
            return true;
        });

        popupMenu.show();
    }

    private void showDeleteCategoryDialog(String categoryId, String categoryName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Category");
        builder.setMessage("Are you sure you want to delete \"" + categoryName + "\"?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            myDB.deleteCategory(categoryId);
            displayCategories();
            Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showEditCategoryDialog(String categoryId, String categoryName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Category");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        final EditText inputCatName = new EditText(this);
        inputCatName.setText(categoryName);
        layout.addView(inputCatName);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newCategoryName = inputCatName.getText().toString().trim();
            if (newCategoryName.isEmpty()) {
                Toast.makeText(this, "Category name cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            myDB.updateCategory(categoryId, newCategoryName);
            displayCategories();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
