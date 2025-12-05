package com.example.travelPlanAgent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity11 extends AppCompatActivity {

    TextView tvSummary, tvPlan, tvRestaurants, tvActivities, tvFlight, tvHotels, tvDestinationTitle;
    Button btnBookFlight;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        db = FirebaseFirestore.getInstance();

        tvSummary = findViewById(R.id.tvAISummary);
        tvPlan = findViewById(R.id.tvItinerary);
        tvRestaurants = findViewById(R.id.tvRestaurants);
        tvActivities = findViewById(R.id.tvActivities);
        tvFlight = findViewById(R.id.tvFlight);
        tvHotels = findViewById(R.id.tvHotels);
        tvDestinationTitle = findViewById(R.id.tvDestinationTitle);
        btnBookFlight = findViewById(R.id.btnBookFlight);

        boolean isViewMode = getIntent().getBooleanExtra("isViewMode", false);

        if (isViewMode) {
            tvDestinationTitle.setText("Saved Trip Plan");
            tvSummary.setText(getIntent().getStringExtra("summary"));
            tvPlan.setText(getIntent().getStringExtra("dayPlan"));
            tvRestaurants.setText(getIntent().getStringExtra("restaurants"));
            tvActivities.setText(getIntent().getStringExtra("activities"));
            tvFlight.setText(getIntent().getStringExtra("flight"));
            tvHotels.setText(getIntent().getStringExtra("hotels"));
        } else {
            String prompt = getIntent().getStringExtra("PROMPT");
            if (prompt == null || prompt.isEmpty()) {
                prompt = "Create a travel plan...";
            }
            tvSummary.setText("Generating your AI-powered travel plan...");

            GeminiClient.generatePlan(prompt, response ->
                    runOnUiThread(() -> mapAIResponse(response))
            );
        }

        btnBookFlight.setOnClickListener(v -> {
            String flightUrl = "http://google.com/travel/flights";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flightUrl));
            startActivity(browserIntent);
        });
    }

    private void mapAIResponse(String aiText) {
        if (!aiText.contains("SUMMARY:")) {
            tvSummary.setText("Your personalized trip plan:");
            tvPlan.setText(aiText);
            return;
        }

        try {
            String summary = extractSection(aiText, "SUMMARY:");
            String dayPlan = extractSection(aiText, "DAY PLAN:");
            String restaurants = extractSection(aiText, "RESTAURANTS:");
            String activities = extractSection(aiText, "ACTIVITIES & EVENTS:");
            String flight = extractSection(aiText, "FLIGHT:");
            String hotels = extractSection(aiText, "HOTELS:");

            tvSummary.setText(summary);
            tvPlan.setText(dayPlan);
            tvRestaurants.setText(restaurants);
            tvActivities.setText(activities);
            tvFlight.setText(flight);
            tvHotels.setText(hotels);

            savePlanToFirestore(summary, dayPlan, restaurants, activities, flight, hotels);

        } catch (Exception e) {
            tvSummary.setText("AI response could not be organized.");
            tvPlan.setText(aiText);
            Log.e("GeminiError", "Parsing error", e);
        }
    }

    private void savePlanToFirestore(String summary, String dayPlan, String restaurants, String activities, String flight, String hotels) {
        Map<String, Object> tripData = new HashMap<>();
        tripData.put("summary", summary);
        tripData.put("dayPlan", dayPlan);
        tripData.put("restaurants", restaurants);
        tripData.put("activities", activities);
        tripData.put("flight", flight);
        tripData.put("hotels", hotels);
        tripData.put("timestamp", System.currentTimeMillis());

        db.collection("plans")
                .add(tripData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity11.this, "Trip Plan Saved! ✅", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity11.this, "Failed to save plan ❌", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Error adding document", e);
                });
    }

    private String extractSection(String fullText, String sectionTitle) {
        int startIndex = fullText.indexOf(sectionTitle);
        if (startIndex == -1) return "Not available";

        startIndex += sectionTitle.length();
        int endIndex = fullText.length();

        String[] possibleNextSections = {
                "SUMMARY:", "DAY PLAN:", "RESTAURANTS:",
                "ACTIVITIES & EVENTS:", "FLIGHT:", "HOTELS:"
        };

        for (String next : possibleNextSections) {
            int tempIndex = fullText.indexOf(next, startIndex);
            if (tempIndex != -1 && tempIndex < endIndex) {
                endIndex = tempIndex;
            }
        }
        return fullText.substring(startIndex, endIndex).trim();
    }
}