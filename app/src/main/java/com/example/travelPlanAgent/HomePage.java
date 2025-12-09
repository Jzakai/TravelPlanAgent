package com.example.travelPlanAgent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

public class HomePage extends AppCompatActivity {

    private Spinner spinnerPurpose, spinnerBudget, spinnerFlight, spinnerHotel,
            spinnerCuisine, spinnerActivity, spinnerWeather, spinnerDestination;
    private EditText etDuration, etTravelers;
    private Button btnFindTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        spinnerPurpose = findViewById(R.id.spinnerPurpose);
        spinnerBudget = findViewById(R.id.spinnerBudget);
        spinnerFlight = findViewById(R.id.spinnerFlight);
        spinnerHotel = findViewById(R.id.spinnerHotel);
        spinnerCuisine = findViewById(R.id.spinnerCuisine);
        spinnerActivity = findViewById(R.id.spinnerActivity);
        spinnerWeather = findViewById(R.id.spinnerWeather);
        spinnerDestination = findViewById(R.id.spinnerDest);

        etDuration = findViewById(R.id.etDuration);
        etTravelers = findViewById(R.id.etTravelers);

        btnFindTrip = findViewById(R.id.btnFindTrip);

        setupSpinner(spinnerPurpose, R.array.purpose_options, "Select purpose");
        setupSpinner(spinnerBudget, R.array.budget_options, "Select budget range");
        setupSpinner(spinnerFlight, R.array.flight_class_options, "Select class");
        setupSpinner(spinnerHotel, R.array.hotel_type_options, "Select type");
        setupSpinner(spinnerCuisine, R.array.cuisine_options, "Select cuisine");
        setupSpinner(spinnerActivity, R.array.activity_options, "Select activity");
        setupSpinner(spinnerWeather, R.array.weather_options, "Select weather");
        setupSpinner(spinnerDestination, R.array.city_options, "Select destination");

        btnFindTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String destination = spinnerDestination.getSelectedItem().toString();
                String purpose = spinnerPurpose.getSelectedItem().toString();
                String budget = spinnerBudget.getSelectedItem().toString();
                String flight = spinnerFlight.getSelectedItem().toString();
                String hotel = spinnerHotel.getSelectedItem().toString();
                String cuisine = spinnerCuisine.getSelectedItem().toString();
                String activity = spinnerActivity.getSelectedItem().toString();
                String weather = spinnerWeather.getSelectedItem().toString();

                String duration = etDuration.getText().toString();
                String travelers = etTravelers.getText().toString();

                if (duration.isEmpty() || travelers.isEmpty()) {
                    Toast.makeText(HomePage.this,
                            "Please fill in duration and number of travelers",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String prompt =
                        "You are a strict JSON-style travel planner.\n" +
                                "Create a travel plan for " + destination + " with these preferences:\n\n" +
                                "Purpose: " + purpose + "\n" +
                                "Budget: " + budget + "\n" +
                                "Flight: " + flight + "\n" +
                                "Hotel: " + hotel + "\n" +
                                "Cuisine: " + cuisine + "\n" +
                                "Activity: " + activity + "\n" +
                                "Weather: " + weather + "\n" +
                                "Duration: " + duration + " days\n" +
                                "Travelers: " + travelers + "\n\n" +
                                "IMPORTANT: You MUST respond USING THIS EXACT FORMAT ONLY.\n" +
                                "DO NOT add introductions. DO NOT add markdown. DO NOT add stars.\n\n" +
                                "SUMMARY:\n<one paragraph>\n\n" +
                                "DAY PLAN:\n" +
                                "Day 1 - ...\n" +
                                "Day 2 - ...\n\n" +
                                "RESTAURANTS:\n" +
                                "- Restaurant 1\n" +
                                "- Restaurant 2\n\n" +
                                "ACTIVITIES & EVENTS:\n" +
                                "- Activity 1\n" +
                                "- Event 1\n\n" +
                                "FLIGHT:\n" +
                                "- Flight option from Riyadh\n\n" +
                                "HOTELS:\n" +
                                "- Hotel 1\n" +
                                "- Hotel 2\n";

                Intent intent = new Intent(HomePage.this, PlanPage.class);
                intent.putExtra("PROMPT", prompt);
                intent.putExtra("DESTINATION", destination);
                intent.putExtra("DURATION", duration);
                startActivity(intent);
            }
        });
    }

    private void setupSpinner(Spinner spinner, int arrayResId, String hint) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                arrayResId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
}