package com.system.myapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Step3Fragment extends Fragment {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private Button maleButton;
    private Button femaleButton;
    private Button nextButton;
    private UserData userData;

    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    public Step3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        emailEditText = view.findViewById(R.id.editTextEmail);
        nextButton = view.findViewById(R.id.nextButton);

        // Replace "your-server-url.com" with the actual URL of your server and PHP script
        String serverUrl = "http://192.168.1.37/recyclearn/report_user/get_user_details.php";
        String userId = "1"; // Replace this with the actual user ID you want to fetch

        // Retrieve the user details from the server
        new RetrieveUserDetailsTask().execute(serverUrl, userId);

        // Find the Yes and No buttons
        maleButton = view.findViewById(R.id.maleButton);
        femaleButton = view.findViewById(R.id.femaleButton);

        // Change the background color of the Yes button to colorPrimary
        maleButton.setBackgroundResource(R.drawable.yes_toggle_background);
        // Revert the background color of the No button to the default color
        femaleButton.setBackgroundResource(R.drawable.button_selector);

        // Change the text color of the No button to colorPrimary
        femaleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        // Revert the text color of the Yes button to the default color
        maleButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        // Set click listeners for the buttons
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the background color of the Yes button to colorPrimary
                maleButton.setBackgroundResource(R.drawable.yes_toggle_background);
                // Revert the background color of the No button to the default color
                femaleButton.setBackgroundResource(R.drawable.button_selector);

                // Change the text color of the No button to colorPrimary
                femaleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                // Revert the text color of the Yes button to the default color
                maleButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

                // Apply the click animation
                applyClickAnimation(R.animator.button_scale, v);
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the background color of the No button to colorPrimary
                femaleButton.setBackgroundResource(R.drawable.yes_toggle_background);
                // Revert the background color of the Yes button to the default color
                maleButton.setBackgroundResource(R.drawable.button_selector);

                // Change the text color of the Yes button to colorPrimary
                maleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                // Revert the text color of the No button to the default color
                femaleButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

                // Apply the click animation
                applyClickAnimation(R.animator.button_scale, v);
            }
        });
        Button backButton = view.findViewById(R.id.backButton);
        // Inside Step2Fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((createReport_activity) requireActivity()).navigateToPreviousFragment(new Step2Fragment());
            }
        });

        // Inside Step3Fragment.java
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the user input here if needed

                // Save the user's input to the userData object
                userData.setPersonalDetails(firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString());

                // Show a success message
                ((createReport_activity) requireActivity()).showFormComplete();

                // Note: You can also choose to navigate to another fragment if there are more steps after Step3
            }
        });


    }

    private void applyClickAnimation(@AnimatorRes int animationResId, View view) {
        Animator animator = AnimatorInflater.loadAnimator(requireContext(), animationResId);
        animator.setTarget(view);
        animator.start();
    }

    private class RetrieveUserDetailsTask extends AsyncTask<String, Void, UserDetails> {

        @Override
        protected UserDetails doInBackground(String... params) {
            String serverUrl = params[0];
            String userId = params[1];

            try {
                URL url = new URL(serverUrl + "?userId=" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response from the server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Log the server response for debugging
                    Log.d("Step3Fragment", "Server Response: " + response.toString());

                    // Parse the JSON response and create a UserDetails object
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    String email = jsonObject.getString("email");

                    return new UserDetails(firstName, lastName, email);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserDetails userDetails) {
            if (userDetails != null) {
                // Set the user details in the EditText fields
                firstNameEditText.setText(userDetails.getFirstName());
                lastNameEditText.setText(userDetails.getLastName());
                emailEditText.setText(userDetails.getEmail());
            }
        }
    }

}
