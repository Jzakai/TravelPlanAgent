package com.example.travelPlanAgent;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MainActivity9 extends AppCompatActivity {

    RecyclerView recyclerView;
    PlanAdapter adapter;
    List<PlanModel> planList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        recyclerView = findViewById(R.id.recyclerViewPlans);
        if (recyclerView == null) {
            Toast.makeText(this, "Error: RecyclerView not found in XML!", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planList = new ArrayList<>();
        adapter = new PlanAdapter(this, planList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchPlans();
    }

    private void fetchPlans() {
        db.collection("plans")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    planList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            PlanModel p = d.toObject(PlanModel.class);
                            planList.add(p);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "No plans saved yet.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}