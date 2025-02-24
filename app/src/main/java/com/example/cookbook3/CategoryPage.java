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
    }
}