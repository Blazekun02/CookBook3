package com.example.cookbook3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CategoryPage extends AppCompatActivity {

    ImageButton returnBtn;
    Button addF;
    Spinner sspinner;
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

        returnBtn = findViewById(R.id.returnBtn);
        addF = findViewById(R.id.addBtn);
        sspinner = findViewById(R.id.sampleSpinner);


        returnBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(CategoryPage.this, com.example.cookbook3.MainActivity.class);
            startActivity(intent1);
        });

        addF.setOnClickListener(v -> showAddCategoryDialog());

    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Category");

        // Layout for inputs
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        // Category Name Input
        final EditText inputCatName = new EditText(this);
        inputCatName.setHint("Enter subject name");
        layout.addView(inputCatName);

        builder.setView(layout);

        // Create the dialog
        AlertDialog dialog = builder.create();

        dialog.show(); // Show the dialog first

        // Create a horizontal layout for buttons
        LinearLayout buttonLayout = new LinearLayout(dialog.getContext());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.END);
        buttonLayout.setPadding(0, 20, 0, 0);

        // Layout parameters for buttons with spacing
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(20, 0, 0, 0); // Add margin between buttons

        // "Cancel" button
        Button cancelButton = new Button(dialog.getContext());
        cancelButton.setText("Cancel");
        cancelButton.setTextSize(16);
        cancelButton.setPadding(20, 10, 20, 10);
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        cancelButton.setTextColor(Color.WHITE);
        cancelButton.setLayoutParams(buttonParams);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // "Add" button
        Button addButton = new Button(dialog.getContext());
        addButton.setText("Add");
        addButton.setTextSize(16);
        addButton.setPadding(20, 10, 20, 10);
        addButton.setBackgroundColor(Color.parseColor("#e7af41")); // Yellow button
        addButton.setTextColor(Color.WHITE);
        addButton.setLayoutParams(buttonParams);

        // Add buttons to layout
        buttonLayout.addView(cancelButton);
        buttonLayout.addView(addButton);

        layout.addView(buttonLayout); // Add buttons to main layout

        // Custom "Add" button validation
        addButton.setOnClickListener(v -> {
            String categoryName = inputCatName.getText().toString().trim();

            // Validate inputs
            if (categoryName.isEmpty()) {
                inputCatName.setError("Category name cannot be empty!");
                return;
            }
            if (isCategoryAlreadyExists(categoryName)) {
                Toast.makeText(v.getContext(), "Subject already exists! Choose a different name.", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss(); // Close dialog only when successful
        });
    }

    private boolean isCategoryAlreadyExists(String categoryName) {
        // Check if the category already exists in the database
        // Return true if exists, false otherwise
        return false;
    }
}