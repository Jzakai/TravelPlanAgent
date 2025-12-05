package com.example.travelPlanAgent;

public class PlanModel {
    private String summary, dayPlan, restaurants, activities, flight, hotels;
    private long timestamp;

    public PlanModel() {}

    public PlanModel(String summary, String dayPlan, String restaurants, String activities, String flight, String hotels, long timestamp) {
        this.summary = summary;
        this.dayPlan = dayPlan;
        this.restaurants = restaurants;
        this.activities = activities;
        this.flight = flight;
        this.hotels = hotels;
        this.timestamp = timestamp;
    }

    public String getSummary() { return summary; }
    public String getDayPlan() { return dayPlan; }
    public String getRestaurants() { return restaurants; }
    public String getActivities() { return activities; }
    public String getFlight() { return flight; }
    public String getHotels() { return hotels; }
    public long getTimestamp() { return timestamp; }
}