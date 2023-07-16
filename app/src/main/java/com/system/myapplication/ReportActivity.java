package com.system.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<Report> reportList;
    private static final String API_URL = "http://192.168.1.12/recyclearn/report_user/get_reports.php?userId=";
    private static final String DELETE_API_URL = "http://192.168.1.12/recyclearn/report_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(reportList);
        recyclerView.setAdapter(reportAdapter);

        // Set item click listener
        reportAdapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("Report Adapter", "A report was clicked"); // Log the image URL
                // Handle item click here
                // Start a new activity to display the report details
                // Pass the report ID or other identifier as an intent extra
            }
        });

        // Set edit click listener
        reportAdapter.setOnEditClickListener(new ReportAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position) {
                // Handle edit click here
                Report report = reportList.get(position);
                String reportId = report.getReportId();

                // Start a new activity to edit the report details
                Intent intent = new Intent(ReportActivity.this, EditReportActivity.class);
                intent.putExtra("reportId", reportId);
                startActivity(intent);
                Log.d("Report Adapter", "Edit report with report ID of : " + reportId); // Log the image URL
            }
        });
        // Set delete click listener in the adapter
        reportAdapter.setOnDeleteClickListener(new ReportAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Show a popup asking the user if they want to delete the report
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setTitle("Delete Report");
                builder.setMessage("Are you sure you want to delete this report?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the report
                        if (position >= 0 && position < reportList.size()) {
                            Report report = reportList.get(position);
                            String reportId = report.getReportId();

                            // Make an API call or execute a database query to delete the report with the given reportId
                            // Example code:
                            deleteReportFromDatabase(reportId);

                            // Update the UI by removing the item from the reportList and notifying the adapter
                            reportList.remove(position);
                            reportAdapter.notifyItemRemoved(position);
                            Log.d("Report Adapter", "Delete report with report ID of: " + reportId + " with position of " + position); // Log the image URL
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }

        });

        Button createReportButton = findViewById(R.id.createReportButton);
        createReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the MainActivity to create a new report
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d("Button", "Create Report Button was clicked"); // Log the image URL
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteReportFromDatabase(final String reportId) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    URL url = new URL(DELETE_API_URL + "/delete_report.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Create JSON object with the reportId
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("reportId", reportId);
                    Log.e("Report ID:", String.valueOf(requestBody));
                    Log.d("Delete Report Method", "Delete report from database with report ID of: " + requestBody); // Log the image URL
                    // Write the JSON data to the request body
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(requestBody.toString());
                    writer.flush();
                    writer.close();

                    int responseCode = connection.getResponseCode();

                    // Log the response code
                    Log.e("Response Code:", String.valueOf(responseCode));

                    // Read the response from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Log the response from the server
                    Log.e("Server Response:", response.toString());

                    // Return true if the request was successful (e.g., HTTP 200 OK)
                    return responseCode == HttpURLConnection.HTTP_OK;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    // Report deleted successfully
                    // Update the UI by removing the item from the reportList and notifying the adapter
                    int position = findReportPosition(reportId);
                    if (position >= 0) {
                        reportList.remove(position);
                        reportAdapter.notifyItemRemoved(position);
                    }
                } else {
                    // Error occurred while deleting the report
                    // Show an error message to the user or handle the error as needed
                    // You can access the error message from the server response
                    String errorMessage = "Failed to delete report";
                    // ...
                }
            }
        }.execute();
    }






    private int findReportPosition(String reportId) {
        for (int i = 0; i < reportList.size(); i++) {
            Report report = reportList.get(i);
            if (report.getReportId().equals(reportId)) {
                return i;
            }
        }
        return -1; // Report not found
    }



    private void fetchReports(final String userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_URL + userId);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    connection.disconnect();

                    final String jsonResponse = response.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parseReportResponse(jsonResponse);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve user ID from the intent or wherever you store it
        String userId = "1"; // Replace with the actual user ID

        fetchReports(userId);
    }
    private void parseReportResponse(String response) {
        // Clear the existing report list before parsing the new response
        reportList.clear();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String reportId = jsonObject.getString("id"); // Use "id" instead of "reportId"
                String description = jsonObject.getString("description");
                String location = jsonObject.getString("location");
                String date = jsonObject.getString("date");
                String time = jsonObject.getString("time");

                Report report = new Report(reportId, description, location, date, time);
                reportList.add(report);
            }

            reportAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
