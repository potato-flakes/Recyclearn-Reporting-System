package com.system.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class EditReportActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private EditText locationEditText;
    private EditText datetimeEditText;
    private Button saveButton;
    private List<String> imageUrls = new ArrayList<>();
    private List<String> ReUploadImageUrls = new ArrayList<>();
    private List<String> deletedImageUrls = new ArrayList<>();
    private List<String> newImageUrls = new ArrayList<>();
    private static final int PICK_IMAGES_REQUEST_CODE = 1;
    private LinearLayout imageContainer;
    private String reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        // Retrieve the reportId from the intent
        reportId = getIntent().getStringExtra("reportId");
        Log.e("Report ID", reportId);

        // Initialize views
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        datetimeEditText = findViewById(R.id.datetimeEditText);
        saveButton = findViewById(R.id.saveButton);
        imageContainer = findViewById(R.id.imageLayout);

        imageUrls = new ArrayList<>();

        Button addImageButton = findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Retrieve the existing report details based on the reportId
        retrieveReportDetailsFromServer(reportId, new ReportCallback() {
            @Override
            public void onReportLoaded(Report report) {
                if (report != null) {
                    // Set the existing details in the EditText fields
                    descriptionEditText.setText(report.getDescription());
                    locationEditText.setText(report.getLocation());
                    datetimeEditText.setText(report.getDate());

                    // Add the retrieved image URLs to the imageUrls list
                    imageUrls.addAll(report.getImageUrls());

                    // Display the images
                    displayImages();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated details from the input fields
                String updatedDescription = descriptionEditText.getText().toString().trim();
                String updatedLocation = locationEditText.getText().toString().trim();
                String updatedDatetime = datetimeEditText.getText().toString().trim();

                // Pass the updated details and deletedImageUrls to the updateReportDetails method
                updateReportDetails(reportId, updatedDescription, updatedLocation, updatedDatetime);
            }
        });

    }

    private void retrieveReportDetailsFromServer(final String reportId, final ReportCallback callback) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Report> task = new AsyncTask<Void, Void, Report>() {
            @Override
            protected Report doInBackground(Void... voids) {
                // Retrieve the report details from the server using the reportId
                // Make an HTTP request to the server and parse the response JSON to create a Report object
                // Return the Report object with the retrieved details

                // Example of retrieving report details from the server
                String apiUrl = "http://192.168.1.6/recyclearn/report_user/get_report_details.php?reportId=" + reportId;

                // Perform the HTTP request and retrieve the response
                String jsonResponse = performHttpRequest(apiUrl);

                // Parse the JSON and create a Report object
                Report report = null;
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String description = jsonObject.getString("description");
                    String location = jsonObject.getString("location");
                    String date = jsonObject.getString("date");
                    String time = jsonObject.getString("time");
                    report = new Report(reportId, description, location, date, time);

                    if (jsonObject.has("imagePaths")) {
                        Log.d("Retrieve JSON Response", jsonObject.toString()); // Debug log to check the JSON response
                        // Retrieve the image paths associated with the report ID
                        JSONArray imagePathsArray = jsonObject.getJSONArray("imagePaths");
                        for (int i = 0; i < imagePathsArray.length(); i++) {
                            String imagePath = imagePathsArray.getString(i);
                            String imageUrl = "http://192.168.1.6/recyclearn/report_user/images/" + imagePath; // Modify the URL as per your server setup
                            report.addImageUrl(imageUrl);
                            Log.d("Retrieved Images: ", imagePath); // Debug log to check the JSON response
                        }

                    }  else {
                        Log.e("JSON", "No value for imagePaths key");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON", "Error parsing JSON: " + e.getMessage());
                    Log.e("HTML", "HTML error response: " + jsonResponse);
                }

                return report;
            }

            @Override
            protected void onPostExecute(Report report) {
                if (callback != null) {
                    callback.onReportLoaded(report);
                }
            }
        };

        task.execute();
    }



    private String performHttpRequest(String apiUrl) {
        // Perform the HTTP request to the server and retrieve the response
        // You can use a networking library like Retrofit, Volley, or OkHttp to simplify the HTTP request process
        // In this example, we'll use HttpURLConnection for simplicity

        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EditReportActivity.this, "Failed to retrieve report details", Toast.LENGTH_SHORT).show();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response.toString();
    }

    private void updateReportDetails(final String reportId, final String updatedDescription, final String updatedLocation, final String updatedDatetime) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                // Implement the logic to update the report details on the server using the reportId
                // Make an HTTP request to the server with the updated details and image URLs

                // Example of updating report details on the server
                String apiUrl = "http://192.168.1.6/recyclearn/report_user/update_report.php";

                // Create a JSON object with the updated details and image URLs
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("reportId", reportId);
                    jsonParams.put("description", updatedDescription);
                    jsonParams.put("location", updatedLocation);
                    jsonParams.put("datetime", updatedDatetime);
                    jsonParams.put("deletedImageUrls", new JSONArray(deletedImageUrls)); // Convert deletedImageUrls list to JSONArray
                    Log.d("Upload JSON Response:", jsonParams.toString()); // Debug log to check the JSON response

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Perform the HTTP POST request to update the report details
                boolean success = performHttpPost(apiUrl, jsonParams.toString());
                uploadImagesToServer();
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    // Report details updated successfully
                    Toast.makeText(EditReportActivity.this, "Report details updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Failed to update report details
                    Toast.makeText(EditReportActivity.this, "Failed to update report details", Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute();
    }
    private boolean uploadImagesToServer() {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        String url = "http://192.168.1.6/recyclearn/report_user/upload.php";
        boolean success = true;
        final AtomicInteger uploadCounter = new AtomicInteger(0);
        for (final String imageUrl : newImageUrls) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Server response", "Condition: " + response);
                            if (Objects.equals(response, "success")) {
                                Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                int count = uploadCounter.incrementAndGet();
                                if (count == newImageUrls.size()) {
                                    // All images have been uploaded
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Your report has been updated", Toast.LENGTH_SHORT).show();
                                            Log.d("Upload image", "Number of uploaded images : " + count);
                                            finish();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    if (imageUrl != null) {
                        try {
                            Bitmap imageBitmap = getBitmapFromUri(Uri.parse(imageUrl));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageData = baos.toByteArray();

                            String encodedImageData = Base64.encodeToString(imageData, Base64.DEFAULT);
                            paramV.put("images", encodedImageData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    paramV.put("reportId", reportId);
                    Log.e("Upload image:", "ReportID on database: " + reportId);
                    return paramV;
                }
            };

            queue.add(stringRequest);
        }

        return success;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return bitmap;
    }

    private boolean performHttpPost(String apiUrl, String jsonParams) {
        // Perform the HTTP request to the server and retrieve the response
        // You can use a networking library like Retrofit, Volley, or OkHttp to simplify the HTTP request process
        // In this example, we'll use HttpURLConnection for simplicity

        boolean success = false;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonParams.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EditReportActivity.this, "Failed to update report details", Toast.LENGTH_SHORT).show();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return success;
    }

    private boolean isValidInput(String description, String location, String datetime) {
        // Perform validation on the input fields
        // Return true if the input is valid, otherwise false
        // You need to implement this method based on your validation logic

        // Replace the below example code with your implementation
        return !description.isEmpty() && !location.isEmpty() && !datetime.isEmpty();
    }

    // Rest of the code remains unchanged
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                // Clear the imageUrls list before adding new images
                imageUrls.clear();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    // Process the imageUri and upload the image to your server
                    // Obtain the URL of the uploaded image and add it to the imageUrls list
                    // Example:
                    // Add the image Uri to the newImageUrls list
                    newImageUrls.add(imageUri.toString());
                    Log.d("PICK_IMAGES_REQUEST_CODE", "Image URL: " + newImageUrls); // Log the image URL
                }
            } else if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                // Process the imageUri and upload the image to your server
                // Obtain the URL of the uploaded image and add it to the imageUrls list
                // Example:
                newImageUrls.add(imageUri.toString());
                Log.d("PICK_IMAGES_REQUEST_CODE", "Image URL: " + newImageUrls); // Log the image URL
            }
            // Display the selected images in the LinearLayout container
            displayImages();
        } else {
            // Log an error message if the result code or request code doesn't match
            Log.e("onActivityResult", "Failed to pick images. Result code: " + resultCode + ", Request code: " + requestCode);
        }
    }


    private void displayImages() {
        // Clear the imageContainer before adding the new images
        imageContainer.removeAllViews();

        // Load and display the images from the imageUrls list
        for (final String imageUrl : imageUrls) {
            // Create a new FrameLayout to hold the ImageView and delete button
            FrameLayout imageLayout = new FrameLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.image_left_margin),
                    getResources().getDimensionPixelSize(R.dimen.image_top_margin),
                    getResources().getDimensionPixelSize(R.dimen.image_right_margin),
                    getResources().getDimensionPixelSize(R.dimen.image_bot_margin)
            );
            imageLayout.setLayoutParams(layoutParams);

            // Create a new ImageView for the image
            ImageView imageView = new ImageView(this);
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_width),
                    getResources().getDimensionPixelSize(R.dimen.image_height)
            );
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Load the image using your preferred library (e.g., Picasso, Glide, etc.)
            // Example with Picasso:
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(imageView);

            // Create a new delete button
            Button deleteButton = new Button(this);
            FrameLayout.LayoutParams deleteButtonParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            deleteButtonParams.gravity = Gravity.TOP | Gravity.END; // Position in top right corner
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setText("Delete"); // Set your delete button text here

            // Set a click listener for the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the image URL from imageUrls list
                    imageUrls.remove(imageUrl);
                    String imagePath = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                    Log.e("I dedelete na image:", imagePath);
                    // Add the deleted image URL to the deletedImageUrls list
                    deletedImageUrls.add(imagePath);
                    Log.e("Deleted Images:", String.valueOf(deletedImageUrls));
                    // Update the image display
                    displayImages();
                }
            });

            // Add the ImageView and delete button to the imageLayout
            imageLayout.addView(imageView);
            imageLayout.addView(deleteButton);

            // Add the imageLayout to the imageContainer
            imageContainer.addView(imageLayout);
        }

        // Load and display the images from the imageUrls list
        for (final String newImageUrl : newImageUrls) {
            // Create a new FrameLayout to hold the ImageView and delete button
            FrameLayout imageLayout = new FrameLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.image_left_margin),
                    getResources().getDimensionPixelSize(R.dimen.image_top_margin),
                    getResources().getDimensionPixelSize(R.dimen.image_right_margin),
                    getResources().getDimensionPixelSize(R.dimen.image_bot_margin)
            );
            imageLayout.setLayoutParams(layoutParams);

            // Create a new ImageView for the image
            ImageView imageView = new ImageView(this);
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_width),
                    getResources().getDimensionPixelSize(R.dimen.image_height)
            );
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Load the image using your preferred library (e.g., Picasso, Glide, etc.)
            // Example with Picasso:
            Picasso.get()
                    .load(newImageUrl)
                    .fit()
                    .centerCrop()
                    .into(imageView);

            // Create a new delete button
            Button deleteButton = new Button(this);
            FrameLayout.LayoutParams deleteButtonParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            deleteButtonParams.gravity = Gravity.TOP | Gravity.END; // Position in top right corner
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setText("Delete"); // Set your delete button text here

            // Set a click listener for the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the image URL from newImageUrls list
                    newImageUrls.remove(newImageUrl);
                    String imagePath = newImageUrl.substring(newImageUrl.lastIndexOf('/') + 1);
                    Log.e("I dedelete na image:", imagePath);
                    // Add the deleted image URL to the deletedImageUrls list
                    deletedImageUrls.add(imagePath);
                    // Update the image display
                    displayImages();
                }
            });

            // Add the ImageView and delete button to the imageLayout
            imageLayout.addView(imageView);
            imageLayout.addView(deleteButton);

            // Add the imageLayout to the imageContainer
            imageContainer.addView(imageLayout);
        }
    }
}
