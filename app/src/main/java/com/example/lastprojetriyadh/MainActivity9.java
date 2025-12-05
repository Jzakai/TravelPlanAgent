package com.example.lastprojetriyadh;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.Intent;

public class MainActivity9 extends AppCompatActivity {

    RadioGroup radioGroupCities;
    RadioButton radioLondon, radioRiyadh;
    Button btnContinuePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9); // ملف XML للواجهة

        // ربط العناصر
        radioGroupCities = findViewById(R.id.radioGroupCities);
        radioLondon = findViewById(R.id.radioLondon);
        radioRiyadh = findViewById(R.id.radioRiyadh);

        // زر Continue → يودّي المستخدم لصفحة إنشاء الخطة (Activity 10 مثلاً)
        btnContinuePlan.setOnClickListener(v -> {

            int selected = radioGroupCities.getCheckedRadioButtonId();

            if (selected == -1) {
                // ولا مدينة انضغطت → تجاهل الآن ممكن later نحط Toast
                return;
            }

            Intent i = new Intent(MainActivity9.this, MainActivity10.class);

            if (selected == R.id.radioLondon) {
                i.putExtra("city", "London");
            } else if (selected == R.id.radioRiyadh) {
                i.putExtra("city", "Riyadh");
            }

            startActivity(i);
        });
    }
}
