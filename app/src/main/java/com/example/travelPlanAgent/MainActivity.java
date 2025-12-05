package com.example.travelPlanAgent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnExplore;
    ImageButton btnWishlist, btnLeaderboard, btnLocateLuggage, btnAiAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWishlist = findViewById(R.id.btnWishlist);
        btnAiAssistant = findViewById(R.id.btnAiAssistant);




        //  زر الـWishlist → يفتح الصفحة 9
        btnWishlist.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MainActivity9.class);
            startActivity(i);
        });

        btnAiAssistant.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MainActivity10.class);
            startActivity(i);
        });
    }
}