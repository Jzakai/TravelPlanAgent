package com.example.lastprojetriyadh;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity11 extends AppCompatActivity {

    TextView tvSummary, tvPlan, tvRestaurants, tvActivities, tvFlight, tvHotels, tvDestinationTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        // ✅ Bind UI
        tvSummary = findViewById(R.id.tvAISummary);
        tvPlan = findViewById(R.id.tvItinerary);
        tvRestaurants = findViewById(R.id.tvRestaurants);
        tvActivities = findViewById(R.id.tvActivities);
        tvFlight = findViewById(R.id.tvFlight);
        tvHotels = findViewById(R.id.tvHotels);
        tvDestinationTitle = findViewById(R.id.tvDestinationTitle);

        // ✅ Receive structured PROMPT from MainActivity
        String prompt = getIntent().getStringExtra("PROMPT");

        if (prompt == null || prompt.isEmpty()) {
            prompt =
                    "Create a 5-day family-friendly travel plan for London.\n" +
                            "Budget: Medium\n" +
                            "Hotel: 4-star\n" +
                            "Activities: Cultural & Shopping\n" +
                            "Cuisine: Halal\n" +
                            "Weather: Mild\n\n" +

                            "IMPORTANT: Format your response exactly like this:\n\n" +

                            "SUMMARY:\n...\n\n" +
                            "DAY PLAN:\nDay 1 - ...\nDay 2 - ...\n\n" +
                            "RESTAURANTS:\n- ...\n\n" +
                            "ACTIVITIES & EVENTS:\n- ...\n\n" +
                            "FLIGHT:\n- ...\n\n" +
                            "HOTELS:\n- ...\n";
        }

        tvSummary.setText("Generating your AI-powered travel plan...");

        // ✅ Call Gemini
        GeminiClient.generatePlan(prompt, response ->
                runOnUiThread(() -> mapAIResponse(response))
        );
    }

    // ✅ NOW WE ORGANIZE THE AI CONTENT INTO SECTIONS
    private void mapAIResponse(String aiText) {

        if (!aiText.contains("SUMMARY:")) {
            // ✅ Fallback: AI ignored format → show raw text in Day Plan
            tvSummary.setText("Your personalized trip plan:");
            tvPlan.setText(aiText);
            tvRestaurants.setText("See itinerary for food suggestions.");
            tvActivities.setText("See itinerary for activity suggestions.");
            tvFlight.setText("Flight info included in itinerary.");
            tvHotels.setText("Hotel suggestions included in itinerary.");
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

        } catch (Exception e) {
            tvSummary.setText("AI response could not be organized.");
            tvPlan.setText(aiText);
        }
    }


    // ✅ HELPER FUNCTION TO EXTRACT EACH SECTION
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