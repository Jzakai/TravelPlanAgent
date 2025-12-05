package com.example.travelPlanAgent;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiClient {

    private static final String API_KEY = "AIzaSyDBkEMcuOb3ntX3b4fcHpY-4EBqI3UeN2k";

    public interface GeminiCallback {
        void onResponse(String text);
    }

    public static void generatePlan(String prompt, GeminiCallback callback) {

        new Thread(() -> {
            try {
                URL url = new URL(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String body = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" +
                        prompt.replace("\"", "\\\"") +
                        "\" } ] } ] }";

                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes());
                os.close();

                int responseCode = conn.getResponseCode();
                System.out.println("GEMINI RESPONSE CODE = " + responseCode);

                BufferedReader br;
                if (responseCode >= 200 && responseCode < 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();

                System.out.println("GEMINI RAW RESPONSE = " + result);

                callback.onResponse(parseGeminiResponse(result.toString()));

            } catch (Exception e) {
                e.printStackTrace();
                callback.onResponse("AI Exception: " + e.getMessage());
            }
        }).start();
    }

    private static String parseGeminiResponse(String json) {
        try {
            JSONObject obj = new JSONObject(json);

            // ✅ If Gemini returned an ERROR instead of candidates
            if (obj.has("error")) {
                JSONObject error = obj.getJSONObject("error");
                return "Gemini Error: " + error.getString("message");
            }

            // ✅ Normal success response
            return obj.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

        } catch (Exception e) {
            return "Parsing Error: " + e.getMessage() + "\nRAW: " + json;
        }
    }
}