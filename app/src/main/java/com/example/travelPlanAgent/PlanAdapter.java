package com.example.travelPlanAgent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private Context context;
    private List<PlanModel> planList;

    public PlanAdapter(Context context, List<PlanModel> planList) {
        this.context = context;
        this.planList = planList;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan_card, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanModel plan = planList.get(position);

        holder.tvDestination.setText(plan.getDestination());
        holder.tvDuration.setText(plan.getDuration() + " Days");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(plan.getTimestamp())));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity11.class);
            intent.putExtra("isViewMode", true);
            intent.putExtra("summary", plan.getSummary());
            intent.putExtra("dayPlan", plan.getDayPlan());
            intent.putExtra("restaurants", plan.getRestaurants());
            intent.putExtra("activities", plan.getActivities());
            intent.putExtra("flight", plan.getFlight());
            intent.putExtra("hotels", plan.getHotels());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvDuration, tvDate;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDate = itemView.findViewById(R.id.tvPlanDate);
        }
    }
}