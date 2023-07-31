package com.system.myapplication;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class createReport_activity extends AppCompatActivity implements OnStepCompletionListener {

    private int currentPage = 0; // Start from the first page
    private ProgressBar userProgressBar;
    private TextView textViewProgress;
    private TextView progressLabel;
    private TextView typeLabel;
    private ViewGroup containerLayout;
    private View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);

        userProgressBar = findViewById(R.id.userProgressBar);
        textViewProgress = findViewById(R.id.text_view_progress);
        progressLabel = findViewById(R.id.progressLabel);
        typeLabel = findViewById(R.id.typeLabel);
        containerLayout = findViewById(R.id.container);
        loadingView = getLayoutInflater().inflate(R.layout.loading_screen, containerLayout, false);

        // Start the form by showing the first fragment
        navigateToNextFragment(Step1Fragment.newInstance(currentPage + 1));
    }

    private Handler handler = new Handler();
    // Method to navigate to the next fragment
    public void navigateToNextFragment(Fragment fragment) {
        currentPage++;
        switch (currentPage) {
            case 1:
                showLoadingScreen();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();

                        hideLoadingScreen();

                        progressLabel.setText("Next: Crime Details");
                        typeLabel.setText("Crime Type");
                        replaceFragment(fragment, currentPage + "/3");
                        updateProgress(currentPage * 33); // Update progress with animation

                    }
                    }, 1000); // Delay time in milliseconds (adjust as needed)
                break;
            case 2:
                showLoadingScreen();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();

                        hideLoadingScreen();


                        progressLabel.setText("Next: Personal Details");
                typeLabel.setText("Crime Details");
                replaceFragment(fragment, currentPage + "/3");
                updateProgress(currentPage * 33); // Update progress with animation
                    }
                }, 1000); // Delay time in milliseconds (adjust as needed)
                break;
            case 3:
                showLoadingScreen();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();

                getSupportFragmentManager().executePendingTransactions();
                hideLoadingScreen();

                progressLabel.setText("Next: Summary Details");
                typeLabel.setText("Personal Details");
                replaceFragment(fragment, currentPage + "/3");
                updateProgress(currentPage * 34); // Update progress with animation
                break;
            default:
                // Form completed
                showFormComplete();
                break;
        }
    }

    private void showLoadingScreen() {
        containerLayout.addView(loadingView);
    }

    private void hideLoadingScreen() {
        containerLayout.removeView(loadingView);
    }

    public void navigateToPreviousFragment(Fragment fragment) {
        currentPage--;
        switch (currentPage) {
            case 1:
                progressLabel.setText("Next: Crime Details");
                typeLabel.setText("Crime Type");
                showLoadingScreen();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();

                getSupportFragmentManager().executePendingTransactions();
                hideLoadingScreen();
                replaceFragment(fragment, currentPage + "/3");
                updateProgress(currentPage * 33); // Update progress with animation
                break;
            case 2:
                progressLabel.setText("Next: Personal Details");
                typeLabel.setText("Crime Details");
                replaceFragment(fragment, currentPage + "/3");
                updateProgress(currentPage * 33); // Update progress with animation
                break;
            case 3:
                typeLabel.setText("Personal Details");
                replaceFragment(fragment, currentPage + "/3");
                updateProgress(currentPage * 34); // Update progress with animation
                break;
            default:
                // Handle if the user tries to go back from the first step
                currentPage = 1;
                // You can show a message or handle it as per your app's logic
                break;
        }
    }

    // Method to replace the current fragment in the container and update progress
    void replaceFragment(Fragment fragment, String progressText) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        updateProgressText(progressText);
    }

    // Method to update the progress text
    private void updateProgressText(String progressText) {
        textViewProgress.setText(progressText);
    }

    // Method to update the progress bar
    private void updateProgress(int progress) {
        // Get the current progress value
        int currentProgress = userProgressBar.getProgress();

        // Create an ObjectAnimator to animate the progress change
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(userProgressBar, "progress", currentProgress, progress);
        progressAnimator.setDuration(1000); // Set the animation duration in milliseconds (adjust as needed)
        // Start the progress animation
        progressAnimator.start();
    }


    // Method to handle form completion
    private void showFormComplete() {
        // For example, show a success message
        Toast.makeText(this, "Form completed successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepCompleted(int stepNumber, boolean isCompleted) {
        if (isCompleted) {

        } else {

        }
    }
}
