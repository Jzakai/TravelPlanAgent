package com.example.travelPlanAgent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MenuPage extends AppCompatActivity {

    ImageButton btnSavedPlans,btnAiAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_page);

        btnSavedPlans = findViewById(R.id.btnSavedPlans);
        btnAiAssistant = findViewById(R.id.btnAiAssistant);

        btnSavedPlans.setOnClickListener(v -> {
            Intent i = new Intent(MenuPage.this, TravelPlanList.class);
            startActivity(i);
        });

        btnAiAssistant.setOnClickListener(v -> {
            Intent i = new Intent(MenuPage.this, HomePage.class);
            startActivity(i);
        });
    }
}