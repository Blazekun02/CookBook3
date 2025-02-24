package com.example.cookbook3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton returnbtn;
        Button addF;
        Spinner sspinner;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        returnbtn = findViewById(R.id.returnBtn);
        addF = findViewById(R.id.addBtn);
        sspinner = findViewById(R.id.sampleSpinner);


        returnbtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(RecipeActivity.this, com.example.cookbook3.MainActivity.class);
            startActivity(intent1);
        });
    }
}