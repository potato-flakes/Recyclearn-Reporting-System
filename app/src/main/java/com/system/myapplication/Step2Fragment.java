package com.system.myapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import pl.droidsonroids.gif.GifImageView;


public class Step2Fragment extends Fragment implements LocationSelectionListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private TextView textViewValue;
    private String formattedDate;
    private List<String> barangayOptions = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private static final int PICK_IMAGES_REQUEST_CODE = 1;
    private static final String API_URL = "http://192.168.1.6/recyclearn/report_user/report.php";
    private LinearLayout imageContainer;
    private LinearLayout enterPersonNameLayout;
    private LinearLayout enterPersonEditTextNameLayout;
    private RelativeLayout timeButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapView mapView;
    private Button todayButton;
    private Button yesterdayButton;
    private Button selectDateButton;
    private Button viewMaps;
    private Button barangayTextInputLayouts;
    private Button yesButton;
    private Button noButton;
    private double latitude;
    private double longitude;
    private TextView locationTextView;
    private TextView dateTextView;
    private TextView timeLabel;
    private TextView amTextView;
    private TextView pmTextView;
    private TextView personNameLabel;
    private TextView gifTextView;
    private ImageView hourIncreaseButton;
    private ImageView hourDecreaseButton ;
    private ImageView minuteIncreaseButton;
    private ImageView minuteDecreaseButton;
    private ImageView iconImageView;
    private ImageView locationImageView;
    private ImageView enterPersonNameImageView;
    private EditText hourEditText;
    private EditText minuteEditText;
    private EditText descriptionEditText;
    private EditText enterPersonEditTexts;
    private boolean isLocationSet;
    private Marker userMarker;
    private Switch setLocationButton;
    private double phLatitude = 12.8797;
    private double phLongitude = 121.7740;
    private ProgressBar progressBar;
    private GifImageView loadingImageView;

    private static final int RESULT_OK = Activity.RESULT_OK;
    private UserData userData;
    private LinearLayout animatedLayout;
    private RelativeLayout buttonLayout;
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        textViewValue = view.findViewById(R.id.textViewValue);
        todayButton = view.findViewById(R.id.todayButton);
        yesterdayButton = view.findViewById(R.id.yesterdayButton);
        selectDateButton = view.findViewById(R.id.selectDateButton);
        timeButton = view.findViewById(R.id.timeButton);
        hourEditText = view.findViewById(R.id.hourEditText);
        imageContainer = view.findViewById(R.id.imageLayout);
        mapView = view.findViewById(R.id.mainMapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        viewMaps = view.findViewById(R.id.viewMapsButton);
        locationTextView = view.findViewById(R.id.locationTextView);
        yesterdayButton = view.findViewById(R.id.yesterdayButton);
        selectDateButton = view.findViewById(R.id.selectDateButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        iconImageView = view.findViewById(R.id.iconImageView);
        timeLabel = view.findViewById(R.id.timeLabel);
        hourEditText = view.findViewById(R.id.hourEditText);
        minuteEditText = view.findViewById(R.id.minuteEditText);
        amTextView = view.findViewById(R.id.amTextView);
        pmTextView = view.findViewById(R.id.pmTextView);
        hourIncreaseButton = view.findViewById(R.id.hourIncreaseButton);
        hourDecreaseButton = view.findViewById(R.id.hourDecreaseButton);
        minuteIncreaseButton = view.findViewById(R.id.minuteIncreaseButton);
        minuteDecreaseButton = view.findViewById(R.id.minuteDecreaseButton);
        barangayTextInputLayouts = view.findViewById(R.id.barangaySpinner);
        progressBar = view.findViewById(R.id.progressBar);
        locationImageView = view.findViewById(R.id.locationImageView);
        loadingImageView = view.findViewById(R.id.loadingImageView);
        gifTextView = view.findViewById(R.id.gifTextView);
        enterPersonNameLayout = view.findViewById(R.id.enterPersonNameLayout);
        enterPersonNameImageView = view.findViewById(R.id.enterPersonNameImageView);
        personNameLabel = view.findViewById(R.id.personNameLabel);
        enterPersonEditTextNameLayout = view.findViewById(R.id.enterPersonEditTextNameLayout);
        enterPersonEditTexts = view.findViewById(R.id.enterPersonEditTexts);
        animatedLayout = view.findViewById(R.id.animatedLayout);
        buttonLayout = view.findViewById(R.id.buttonLayout);

        imageUrls = new ArrayList<>();

        // Populate the UI elements with data from the userData object (if available)
        if (userData != null) {
            String crimeType = userData.getCrimeType();
           if (crimeType != null){
               textViewValue.setText(userData.getCrimeType());
           }
            String person = userData.getCrimePerson();
           if (person != null){
               enterPersonEditTexts.setText(userData.getCrimePerson());
            }
            String crimeDate = userData.getCrimeDate();
            if (crimeDate != null) {
                // Update the date buttons based on the stored date
                updateDateButtons(crimeDate);
            }
        } else {
            Log.e("Step1Fragment", "onCreateView - userData is null");
        }


        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            // Inside the onClick() method of the "Next" button in Step2Fragment
            @Override
            public void onClick(View v) {
                // Save the user's input to the userData object
                userData.setCrimePerson(enterPersonEditTexts.getText().toString());

                // Navigate to the next fragment (Step3Fragment)
                ((createReport_activity) requireActivity()).navigateToNextFragment(new Step3Fragment());
            }

        });

        Button addImageButton = view.findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Add Image Button was clicked");
                openImagePicker();
            }
        });

        setLocationButton = view.findViewById(R.id.locationSwitch);
        setLocationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLocation();
                    Log.d("MainActivity", "Switch Button was turned ON");
                } else {
                    // If the switch is turned off, reset the location flag
                    isLocationSet = false;

                    Log.d("MainActivity", "Switch Button was turned OFF");
                    // Remove the previous marker from the map
                    if (userMarker != null) {
                        mapView.getOverlays().remove(userMarker);
                        mapView.invalidate();
                        userMarker = null;
                        Log.d("MainActivity", "User Marker was removed");
                    }
                    // Stop location updates
                    if (locationManager != null && locationListener != null) {
                        locationManager.removeUpdates(locationListener);
                    }
                }
            }
        });
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the selected state for the buttons
                todayButton.setSelected(true);
                yesterdayButton.setSelected(false);
                selectDateButton.setSelected(false);

                // Set the text colors and backgrounds for the buttons
                todayButton.setTextColor(getResources().getColor(R.color.selected_text_color));
                yesterdayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                selectDateButton.setTextColor(getResources().getColor(R.color.unselected_text_color));

                todayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_selected_shape));
                yesterdayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                selectDateButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));

                // Get the current date
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(currentDate.getTime());

                userData.setCrimeDate(formattedDate);

                Log.d("MainActivity", "Today Button was clicked");
                Log.d("MainActivity", "Today's Date is: " + formattedDate);
            }
        });

        yesterdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the selected state for the buttons
                todayButton.setSelected(false);
                yesterdayButton.setSelected(true);
                selectDateButton.setSelected(false);

                // Set the text colors and backgrounds for the buttons
                todayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                yesterdayButton.setTextColor(getResources().getColor(R.color.selected_text_color));
                selectDateButton.setTextColor(getResources().getColor(R.color.unselected_text_color));

                todayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                yesterdayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_selected_shape));
                selectDateButton.setBackground(ContextCompat.getDrawable(requireContext() ,R.drawable.button_background));

                // Get the previous day's date
                Calendar previousDate = Calendar.getInstance();
                previousDate.add(Calendar.DAY_OF_MONTH, -1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(previousDate.getTime());

                userData.setCrimeDate(formattedDate);

                Log.d("MainActivity", "Yesterday Button was clicked");
                Log.d("MainActivity", "Yesterday's Date is: " + formattedDate);
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the selected state for the buttons
                todayButton.setSelected(false);
                yesterdayButton.setSelected(false);
                selectDateButton.setSelected(true);

                // Set the text colors and backgrounds for the buttons
                todayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                yesterdayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                selectDateButton.setTextColor(getResources().getColor(R.color.selected_text_color));

                todayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                yesterdayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                selectDateButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_selected_shape));

                showDatePicker();
            }
        });

        // Find the ImageView by its ID
        ImageView iconImageView = view.findViewById(R.id.iconImageView);

        // Set the click listener for the ImageView
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                onDateIconClick();
            }
        });

        viewMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationBottomSheet();
                Log.d("MainActivity", "View Maps Button was clicked");
            }
        });

        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

// Convert 24-hour format to 12-hour format
        int hour12Format = hourOfDay % 12;
        if (hour12Format == 0) {
            hour12Format = 12; // 0 should be displayed as 12 in 12-hour format
        }

// Set the current time to the EditText fields
        hourEditText.setText(String.valueOf(hour12Format));
        minuteEditText.setText(String.format("%02d", minute));

        Log.d("MainActivity", "Today Button was clicked");
        Log.d("MainActivity", "Current Hour is: " + hourEditText);
        Log.d("MainActivity", "Current Minute is: " + minuteEditText);

        int prmyClr = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        // Set the click listeners for AM and PM TextViews
        if (hourOfDay >= 12) {
            // PM selected
            amTextView.setTextColor(Color.GRAY);
            pmTextView.setTextColor(prmyClr);
        } else {
            // AM selected
            amTextView.setTextColor(prmyClr);
            pmTextView.setTextColor(Color.GRAY);
        }

        hourIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Hour increase Button was clicked");
                int hour = Integer.parseInt(hourEditText.getText().toString());
                if (hour < 12) {
                    hour++;
                } else {
                    hour = 1;
                }
                hourEditText.setText(String.valueOf(hour));
            }
        });

        hourDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Hour decrease Button was clicked");
                int hour = Integer.parseInt(hourEditText.getText().toString());
                if (hour > 1) {
                    hour--;
                } else {
                    hour = 12;
                }
                hourEditText.setText(String.valueOf(hour));
            }
        });

        minuteIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Minute increase Button was clicked");
                int minute = Integer.parseInt(minuteEditText.getText().toString());
                if (minute < 59) {
                    minute++;
                } else {
                    minute = 0;
                }
                minuteEditText.setText(String.format("%02d", minute));
            }
        });

        minuteDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Hour decrease Button was clicked");
                int minute = Integer.parseInt(minuteEditText.getText().toString());
                if (minute > 0) {
                    minute--;
                } else {
                    minute = 59;
                }
                minuteEditText.setText(String.format("%02d", minute));
            }
        });

        // Set the click listeners for AM and PM TextViews
        amTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "AM Button was clicked");
                amTextView.setTextColor(prmyClr);
                pmTextView.setTextColor(Color.GRAY);
            }
        });

        pmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "PM Button was clicked");
                amTextView.setTextColor(Color.GRAY);
                pmTextView.setTextColor(prmyClr);
            }
        });

        barangayOptions.add("Balantacan");
        barangayOptions.add("Bancal Pugad");
        barangayOptions.add("Bancal Sinubli");
        barangayOptions.add("Baruya (San Rafael)");
        barangayOptions.add("Calangain");
        barangayOptions.add("Concepcion");
        barangayOptions.add("De La Paz");
        barangayOptions.add("Del Carmen");
        barangayOptions.add("Don Ignacio Dimson");
        barangayOptions.add("Lourdes (Lauc Pau)");
        barangayOptions.add("Prado Siongco");
        barangayOptions.add("Remedios");
        barangayOptions.add("San Agustin");
        barangayOptions.add("San Antonio");
        barangayOptions.add("San Francisco");
        barangayOptions.add("San Isidro");
        barangayOptions.add("San Jose Apunan");
        barangayOptions.add("San Jose Gumi");
        barangayOptions.add("San Juan (Poblacion)");
        barangayOptions.add("San Matias");
        barangayOptions.add("San Miguel");
        barangayOptions.add("San Nicolas 1st (Poblacion)");
        barangayOptions.add("San Nicolas 2nd");
        barangayOptions.add("San Pablo 1st");
        barangayOptions.add("San Pablo 2nd");
        barangayOptions.add("San Pedro Palcarangan");
        barangayOptions.add("San Pedro Saug");
        barangayOptions.add("San Roque Arbol");
        barangayOptions.add("San Roque Dau");
        barangayOptions.add("San Vicente");
        barangayOptions.add("Santa Barbara");
        barangayOptions.add("Santa Catalina");
        barangayOptions.add("Santa Cruz");
        barangayOptions.add("Santa Lucia (Poblacion)");
        barangayOptions.add("Santa Maria");
        barangayOptions.add("Santa Monica");
        barangayOptions.add("Santa Rita");
        barangayOptions.add("Santa Teresa 1st");
        barangayOptions.add("Santa Teresa 2nd");
        barangayOptions.add("Santiago");
        barangayOptions.add("Santo Cristo");
        barangayOptions.add("Santo Domingo");
        barangayOptions.add("Santo Niño (Prado Saba)");
        barangayOptions.add("Santo Tomas (Poblacion)");

        barangayTextInputLayouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        // Update the map view with a marker at the user's location
        GeoPoint userLocation = new GeoPoint(phLatitude, phLongitude);
        userMarker = new Marker(mapView);
        userMarker.setPosition(userLocation);
        mapView.getOverlays().add(userMarker);

        // Animate to the user's location with zoom
        final double zoomLevel = 2.5; // Set your desired zoom level as a double
        mapView.getController().animateTo(userLocation, zoomLevel, null);

        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker);
            mapView.invalidate();
            userMarker = null;
        }

        LinearLayout typeOfCrimeLayout = view.findViewById(R.id.typeOfCrimeLayout);
        typeOfCrimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              navigateToStep1Fragment();
            }
        });

        // Find the Yes and No buttons
        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);

        // Change the background color of the Yes button to colorPrimary
        yesButton.setBackgroundResource(R.drawable.yes_toggle_background);
        // Revert the background color of the No button to the default color
        noButton.setBackgroundResource(R.drawable.button_selector);

        // Change the text color of the No button to colorPrimary
        noButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        // Revert the text color of the Yes button to the default color
        yesButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        // Set click listeners for the buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the background color of the Yes button to colorPrimary
                yesButton.setBackgroundResource(R.drawable.yes_toggle_background);
                // Revert the background color of the No button to the default color
                noButton.setBackgroundResource(R.drawable.button_selector);

                // Change the text color of the No button to colorPrimary
                noButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                // Revert the text color of the Yes button to the default color
                yesButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

                // Apply the click animation
                applyClickAnimation(R.animator.button_scale, v);

                // Slide up the views using TransitionManager
                TransitionManager.beginDelayedTransition((ViewGroup) animatedLayout.getParent());
                animatedLayout.setVisibility(View.VISIBLE);

                userData.setYesButtonSelected(true);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the background color of the No button to colorPrimary
                noButton.setBackgroundResource(R.drawable.yes_toggle_background);
                // Revert the background color of the Yes button to the default color
                yesButton.setBackgroundResource(R.drawable.button_selector);

                // Change the text color of the Yes button to colorPrimary
                yesButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                // Revert the text color of the No button to the default color
                noButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

                // Apply the click animation
                applyClickAnimation(R.animator.button_scale, v);

                // Slide down the views using TransitionManager
                TransitionManager.beginDelayedTransition((ViewGroup) animatedLayout.getParent());
                animatedLayout.setVisibility(View.GONE);

                userData.setYesButtonSelected(false);
            }
        });
        Button backButton = view.findViewById(R.id.backButton);
        // Inside Step2Fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navigateToStep1Fragment();
            }
        });


        return view;
    }

    private void updateDateButtons(String crimeDate) {
        // Parse the stored date and create a Calendar instance
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        Calendar selectedDate = Calendar.getInstance();
        try {
            selectedDate.setTime(dateFormat.parse(crimeDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get the current date for comparison
        Calendar currentDate = Calendar.getInstance();

        // Check if the stored date is today
        if (selectedDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)) {
            // Set the Today button as selected
            todayButton.setSelected(true);
            yesterdayButton.setSelected(false);
            selectDateButton.setSelected(false);

            // Update the text colors and backgrounds for the buttons accordingly
            todayButton.setTextColor(getResources().getColor(R.color.selected_text_color));
            yesterdayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
            selectDateButton.setTextColor(getResources().getColor(R.color.unselected_text_color));

            todayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_selected_shape));
            yesterdayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
            selectDateButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
        } else {
            // Calculate the previous date for comparison
            Calendar previousDate = Calendar.getInstance();
            previousDate.add(Calendar.DAY_OF_MONTH, -1);

            // Check if the stored date is yesterday
            if (selectedDate.get(Calendar.YEAR) == previousDate.get(Calendar.YEAR) &&
                    selectedDate.get(Calendar.DAY_OF_YEAR) == previousDate.get(Calendar.DAY_OF_YEAR)) {
                // Set the Yesterday button as selected
                todayButton.setSelected(false);
                yesterdayButton.setSelected(true);
                selectDateButton.setSelected(false);

                // Update the text colors and backgrounds for the buttons accordingly
                todayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                yesterdayButton.setTextColor(getResources().getColor(R.color.selected_text_color));
                selectDateButton.setTextColor(getResources().getColor(R.color.unselected_text_color));

                todayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                yesterdayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_selected_shape));
                selectDateButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
            } else {
                // The stored date is neither today nor yesterday, set the Select Date button as selected
                todayButton.setSelected(false);
                yesterdayButton.setSelected(false);
                selectDateButton.setSelected(true);

                // Update the text colors and backgrounds for the buttons accordingly
                todayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                yesterdayButton.setTextColor(getResources().getColor(R.color.unselected_text_color));
                selectDateButton.setTextColor(getResources().getColor(R.color.selected_text_color));

                todayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                yesterdayButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_background));
                selectDateButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_selected_shape));

                animateButtonsToLeft();
            }
        }
    }


    private void updateButtonUI() {
        // Check the isYesButtonSelected state from UserData and update the UI accordingly
        if (userData.isYesButtonSelected()) {
            yesButton.setBackgroundResource(R.drawable.yes_toggle_background);
            noButton.setBackgroundResource(R.drawable.button_selector);
            noButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            yesButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
            animatedLayout.setVisibility(View.VISIBLE);
        } else {
            noButton.setBackgroundResource(R.drawable.yes_toggle_background);
            yesButton.setBackgroundResource(R.drawable.button_selector);
            yesButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            noButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
            animatedLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the button UI based on the stored value in UserData
        updateButtonUI();
    }

    private void applyClickAnimation(@AnimatorRes int animationResId, View view) {
        Animator animator = AnimatorInflater.loadAnimator(requireContext(), animationResId);
        animator.setTarget(view);
        animator.start();
    }

    public void onLocationSelected(double retrievedLatitude, double retrievedLongitude) {

        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker);
            mapView.invalidate();
            userMarker = null;
        }
        Log.d("MainActivity", "onlocationSelected - Latitude retrieved: " + retrievedLatitude);
        Log.d("MainActivity", "onlocationSelected - Longitude retrieved: " + retrievedLongitude);

        // Update the latitude and longitude values
        latitude = retrievedLatitude;
        longitude = retrievedLongitude;
        Log.d("MainActivity", "onlocationSelected - New value of Latitude :" + latitude);
        Log.d("MainActivity", "onlocationSelected - New value of Longitude :" + longitude);

        new Step2Fragment.ConvertCoordinatesTask().execute(latitude, longitude);

        // Update the map view to show the retrieved location
        if (mapView != null) {
            // Update the map view with a marker at the user's location
            GeoPoint userLocation = new GeoPoint(latitude, longitude);
            userMarker = new Marker(mapView);
            userMarker.setPosition(userLocation);
            mapView.getOverlays().add(userMarker);

            // Animate to the user's location with zoom
            final double zoomLevel = 18.5; // Set your desired zoom level as a double
            mapView.getController().animateTo(userLocation, zoomLevel, null);
            // Stop location updates
            if (locationManager != null && locationListener != null) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }

    private void showLocationBottomSheet() {
        // Check if the fragment is already visible
        Fragment existingFragment = requireActivity().getSupportFragmentManager().findFragmentByTag("location_bottom_sheet_fragment");
        if (existingFragment != null && existingFragment.isVisible()) {
            // Fragment is already visible, do not show another instance
            return;
        }

        // Create a new instance of the LocationBottomSheetFragment
        LocationBottomSheetFragment fragment = LocationBottomSheetFragment.newInstance(latitude, longitude);
        Log.d("MainActivity", "showLocationBottomSheet - New value of Latitudess :" + latitude);
        Log.d("MainActivity", "showLocationBottomSheet - New value of Longitudes :" + longitude);
        // Set the LocationSelectionListener on the fragment (which is MainActivity)
        fragment.setLocationSelectionListener(this);

        // Show the fragment using a FragmentManager
        FragmentManager fragmentManager = getChildFragmentManager();
        fragment.show(fragmentManager, "location_bottom_sheet_fragment");
    }


    private void showBottomSheetDialog() {
        // Create a bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());

        // Inflate the bottom sheet dialog content view
        View contentView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_content, null);
        bottomSheetDialog.setContentView(contentView);

        // Find the ListView in the content view
        ListView barangayListView = contentView.findViewById(R.id.barangayListView);

        // Create a custom adapter for the ListView
        CustomDropdownAdapter adapter = new CustomDropdownAdapter(requireContext(), barangayOptions);
        barangayListView.setAdapter(adapter);

        // Set the item click listener for list items
        barangayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedBarangay = barangayOptions.get(position);
                // Handle the selected barangay as desired
                barangayTextInputLayouts.setText(selectedBarangay);
                Log.d("MainActivity", "showBottomSheetDialog - Chosen Barangay :" + barangayTextInputLayouts);
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet dialog
            }
        });

        // Show the bottom sheet dialog
        bottomSheetDialog.show();
    }

    // To get the selected time, you can use the following method
    private String getSelectedTime() {
        int hour = Integer.parseInt(hourEditText.getText().toString());
        int minute = Integer.parseInt(minuteEditText.getText().toString());
        String time = String.format("%02d:%02d", hour, minute);
        if (pmTextView.getCurrentTextColor() == Color.BLACK) {
            // PM selected
            time += " PM";
        } else {
            // AM selected
            time += " AM";
        }
        Log.d("MainActivity", "getSelectedTime - Selected Time :" + time);
        return time;
    }
    private void animateButtonsToLeft() {
        Log.d("MainActivity", "animateButtonsToLeft - has started");

        int translateDistance = -buttonLayout.getWidth();
        Animation animation = new TranslateAnimation(0, translateDistance, 0, 0);
        animation.setDuration(500); // Adjust the duration to make the animation smoother

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Update the button texts with the corresponding date
                showTextViewFromRight(formattedDate);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Hide the buttons after the animation is complete
                todayButton.setVisibility(View.GONE);
                yesterdayButton.setVisibility(View.GONE);
                selectDateButton.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        todayButton.startAnimation(animation);
        yesterdayButton.startAnimation(animation);
        selectDateButton.startAnimation(animation);
    }

    private void showTextViewFromRight(String date) {
        Log.d("MainActivity", "showTextViewFromRight - has started");
        // Set the text for the TextView
        if (userData != null){
            String crimeDate = userData.getCrimeDate();
            if (crimeDate != null){
                date = userData.getCrimeDate();
            }
        }
        dateTextView.setText(date);

        // Update the visibility of the TextView
        dateTextView.setVisibility(View.VISIBLE);
        iconImageView.setVisibility(View.VISIBLE);

        // Animate the TextView from the right side
        Animation animation = new TranslateAnimation(buttonLayout.getWidth(), 0, 0, 0);
        animation.setDuration(500); // Adjust the duration to make the animation smoother
        dateTextView.startAnimation(animation);
    }

    public void onDateIconClick() {
        Log.d("MainActivity", "onIconClick - has started");
        // Animate the dateTextView and iconImageView to the right
        animateTextViewAndIconToRight();

    }

    private void animateTextViewAndIconToRight() {
        Log.d("MainActivity", "animateTextViewAndIconToRight - has started");
        int translateDistance = buttonLayout.getWidth();
        Animation animation = new TranslateAnimation(0, translateDistance, 0, 0);
        animation.setDuration(500); // Adjust the duration to make the animation smoother

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animateButtonsFromLeftToRight();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Hide only the dateTextView after the animation is complete
                dateTextView.setVisibility(View.GONE);
                iconImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        dateTextView.startAnimation(animation);

        // Hide the iconImageView immediately without animation
        iconImageView.setVisibility(View.GONE);
    }

    private void animateButtonsFromLeftToRight() {
        Log.d("MainActivity", "animateButtonsFromLeftToRight - has started");
        Animation animation = new TranslateAnimation(-buttonLayout.getWidth(), 0, 0, 0);
        animation.setDuration(500); // Adjust the duration to make the animation smoother

        todayButton.setVisibility(View.VISIBLE);
        yesterdayButton.setVisibility(View.VISIBLE);
        selectDateButton.setVisibility(View.VISIBLE);

        todayButton.startAnimation(animation);
        yesterdayButton.startAnimation(animation);
        selectDateButton.startAnimation(animation);
    }


    private void showDatePicker() {
        Log.d("MainActivity", "showDatePicker - has started");
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Create a SimpleDateFormat to format the date
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

                // Create a Calendar instance and set it to the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Format the selected date using the SimpleDateFormat
                formattedDate = dateFormat.format(selectedDate.getTime());
                userData.setCrimeDate(formattedDate);

                // Animate the buttons
                animateButtonsToLeft();

                // Show the iconImageView
                iconImageView.setVisibility(View.VISIBLE);
                Log.d("MainActivity", "showDatePicker - Selected Date: " + formattedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void setLocation() {
        Log.d("MainActivity", "setLocation - Set Location Button was clicked");
        // Show the loading GIF
        loadingImageView.setVisibility(View.VISIBLE);

        gifTextView.setVisibility(View.VISIBLE);

        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker);
            mapView.invalidate();
            userMarker = null;
        }

        if (!setLocationButton.isChecked()) {
            Log.e("MainActivity", "Switch button was turned off");
            // If the switch is turned off, reset the location flag and return
            isLocationSet = false;
            // Hide the loading GIF
            loadingImageView.setVisibility(View.GONE);
            gifTextView.setVisibility(View.GONE);

            return;
        }

        Log.e("MainActivity", "Switch button was turned on");

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                // Reverse geocode the latitude and longitude to get the location address
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        // Set the location only if it's not already set

                        if (!isLocationSet) {
                            locationTextView.setText(address.getAddressLine(0));

                            new Step2Fragment.ConvertCoordinatesTask().execute(latitude, longitude);

                            // Update the map view with a marker at the user's location
                            GeoPoint userLocation = new GeoPoint(latitude, longitude);
                            userMarker = new Marker(mapView);
                            userMarker.setPosition(userLocation);
                            mapView.getOverlays().add(userMarker);

                            // Animate to the user's location with zoom
                            final double zoomLevel = 18.5; // Set your desired zoom level as a double
                            mapView.getController().animateTo(userLocation, zoomLevel, null);
                            Log.d("MainActivity", "User location using Geopoint: " + userLocation);
                            // Inside the onLocationChanged() method, after updating the UI, hide the progress bar
                            // Hide the loading GIF
                            loadingImageView.setVisibility(View.GONE);
                            gifTextView.setVisibility(View.GONE);
                            // Update the flag to indicate that the location is set
                            isLocationSet = true;


                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Request location updates only if the permission is granted
        if (setLocationButton.isChecked() && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Log.d("MainActivity", "setLocationMethod - Permission to open location allowed");
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.d("MainActivity", "setLocationMethod - Permission to open location not allowed");
        }
    }
    private Handler handler = new Handler();
    private Runnable hideProgressBarRunnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };

    private class ConvertCoordinatesTask extends AsyncTask<Double, Void, String> {
        private final double LATITUDE_ADJUSTMENT = 0.0001;
        private final double LONGITUDE_ADJUSTMENT = 0.0001;
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE); // Show the progress bar
            progressBar.setIndeterminate(true); // Set the ProgressBar to indeterminate mode
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN); // Set the color of the ProgressBar
            // Disable the button
            barangayTextInputLayouts.setEnabled(false);
            barangayTextInputLayouts.setVisibility(View.GONE); // Show the progress bar
            locationImageView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Double... params) {
            double latitude = params[0];
            double longitude = params[1];
            Log.d("MainActivty", "ConvertCoordinatesTask - Passed latitude value: " + latitude);
            Log.d("MainActivty", "ConvertCoordinatesTask - Passed longitude value: " + longitude);
            String barangay = null;
            int loopCount = 0; // Counter variable for loop iterations

            try {
                double adjustment = LATITUDE_ADJUSTMENT;
                while (barangay == null) {
                    loopCount++; // Increment loop counter

                    String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + latitude + "&lon=" + longitude;
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject addressObject = jsonObject.getJSONObject("address");
                        String village = addressObject.optString("village");
                        if (village != null && !village.isEmpty()) {
                            barangay = addressObject.optString("village");
                            Log.d("MainActivty", "ConvertCoordinatesTask - User barangay using OpenStreets: " + barangay);
                        } else if (barangay == null){
                            // Call the method to load barangays from CSV and get the corresponding barangay name
                            barangay = loadBarangaysFromCSV(latitude, longitude);
                            Log.d("MainActivty", "ConvertCoordinatesTask - loadBarangaysFromCSV has started");
                        }  else {
                            Log.d("MainActivty", "ConvertCoordinatesTask - Adjusting coordinates");
                            // Adjust the latitude and longitude based on the current adjustment value
                            latitude += adjustment;

                            // Toggle the adjustment direction
                            if (adjustment > 0) {
                                adjustment = -adjustment;
                            } else {
                                adjustment = -adjustment + LATITUDE_ADJUSTMENT;
                                longitude += LONGITUDE_ADJUSTMENT;
                            }
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            Log.d("MainActivty", "ConvertCoordinatesTask - Loop count: " + loopCount); // Log the loop count

            return barangay;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Update the location address in the UI
                barangayTextInputLayouts.setText(result);
                Log.d("MainActivty", "ConvertCoordinatesTask - Final detected barangay: " + barangayTextInputLayouts); // Log the loop count
            } else {
                // If the barangay is not found using OpenStreets and CSV,
                // set an appropriate message or handle it as needed
                barangayTextInputLayouts.setText("Barangay not found");
            }
            progressBar.setVisibility(View.GONE); // Hide the progress bar
            // Enable the button
            barangayTextInputLayouts.setEnabled(true);
            barangayTextInputLayouts.setVisibility(View.VISIBLE); // Show the progress bar
            locationImageView.setVisibility(View.VISIBLE);
        }
    }

    private String loadBarangaysFromCSV(double sampleLatitude, double sampleLongitude) {
        try {
            // Open the CSV file from the assets folder
            InputStream inputStream = requireContext().getAssets().open("Barangays.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            Log.e("MainActivity", "loadBarangaysFromCSV - Passed Latitudes value: " + sampleLatitude);
            Log.e("MainActivity", "loadBarangaysFromCSV - Passed Longitude value: " + sampleLongitude);

            // Round off the sampleLatitude and sampleLongitude to their nearest third decimal value
            sampleLatitude = roundToNearestThirdDecimal(sampleLatitude);
            sampleLongitude = roundToNearestThirdDecimal(sampleLongitude);

            Log.e("MainActivity", "loadBarangaysFromCSV - Passed Latitude value: " + String.format("%.3f", sampleLatitude));
            Log.e("MainActivity", "loadBarangaysFromCSV - Passed Longitude value: " + String.format("%.3f", sampleLongitude));

            // Read each line of the CSV file
            String line;
            String correspondingBarangayName = ""; // Variable to store the corresponding barangay name

            while ((line = reader.readLine()) != null) {
                // Split the line by comma
                String[] barangayData = line.split(",");

                // Get the latitude and longitude values from the CSV
                double latitude = Double.parseDouble(barangayData[12]);
                double longitude = Double.parseDouble(barangayData[11]);

                // Check if the latitude and longitude values match the sample location
                if (latitude == sampleLatitude && longitude == sampleLongitude) {
                    // Sample location found
                    correspondingBarangayName = barangayData[4];
                    Log.e("MainActivity", "loadBarangaysFromCSV - Corresponding Barangay name: " + correspondingBarangayName);
                    break; // Exit the loop since we found the sample location
                }
            }

            // Close the reader
            reader.close();

            return correspondingBarangayName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // Return an empty string if no corresponding barangay name is found
    }

    private double roundToNearestThirdDecimal(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }

    // Helper method to convert content URI to Bitmap
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return bitmap;
    }
    private void crimeDetails() {
        // Retrieve user ID from the intent or wherever you store it
        final String userId = "1"; // Replace with the actual user ID
        final String crime_person = enterPersonEditTexts.getText().toString();
        final String crime_selectedDate = formattedDate;
        final String crime_selectedTime = getSelectedTime();
        final String crime_location = barangayTextInputLayouts.getText().toString();
        final String crime_description = descriptionEditText.getText().toString();

        // Save the selected crime type to the userData object
        userData.setCrimePerson(crime_person);
        userData.setCrimeDate(crime_selectedDate);
        userData.setCrimeTime(crime_selectedTime);
        userData.setCrimeLocation(crime_location);
        userData.setCrimeDescription(crime_description);
    }
    private void reportCrime() {
        // Retrieve user ID from the intent or wherever you store it
        final String userId = "1"; // Replace with the actual user ID
        final String crime_type = textViewValue.getText().toString();
        final String crime_person = enterPersonEditTexts.getText().toString();
        final String crime_selectedDate = formattedDate;
        final String crime_selectedTime = getSelectedTime();
        final String crime_location = barangayTextInputLayouts.getText().toString();
        final String crime_description = descriptionEditText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create JSON object with crime data
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", userId);
                    jsonObject.put("description", crime_type);
                    jsonObject.put("description", crime_person);
                    jsonObject.put("date", crime_selectedDate);
                    jsonObject.put("time", crime_selectedTime);
                    jsonObject.put("location", crime_location);
                    jsonObject.put("description", crime_description);

                    Log.d("MainActivity", "reportCrime - Data to be passed: " + jsonObject);

                    // Send the data to the PHP API and get the response
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(jsonObject.toString());
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the response from the API
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Parse the response as JSON
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        Log.d("MainActivity", "reportCrime - JSON Response - Data upload: " + jsonResponse);
                        // Extract the report ID from the response
                        String reportId = jsonResponse.getString("reportId");
                        String message = jsonResponse.getString("message");
                        Log.d("MainActivity", "reportCrime - Retrieved Report ID from server: " + reportId);
                        Log.d("MainActivity", "reportCrime - Retrieved Message from server: " + message);

                        // Upload the images to the server
                        boolean imageUploadSuccess = uploadImagesToServer(imageUrls, reportId);

                        // Inside your Step2Fragment
                        Handler handler = new Handler(Looper.getMainLooper());

                        // To run code on the UI thread, use the handler like this:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (imageUploadSuccess) {
                                    Toast.makeText(requireContext(), "Crime reported successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Failed to upload images", Toast.LENGTH_SHORT).show();
                                    Log.e("MainActivity", "reportCrime - Check uploadImagesToServer method");
                                }
                            }
                        });
                    } else {
                        // Display an error message
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "Response from server does not match", Toast.LENGTH_SHORT).show();
                                Log.e("MainActivity", "reportCrime - Check report.php");
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    // Display an error message
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Please check the inputted details", Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity", "reportCrime - Check inputted detailes");
                        }
                    });
                }
            }
        }).start();
    }

    // Helper method to upload and save the image file on the server
    private boolean uploadImagesToServer(final List<String> imageUrls, final String reportId) {
        Log.d("MainActivity", "uploadImagesToServer - has started");
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "http://192.168.1.6/recyclearn/report_user/upload.php";
        boolean success = true;
        final AtomicInteger uploadCounter = new AtomicInteger(0);
        for (final String imageUrl : imageUrls) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("MainActivity", "uploadImagesToServer - Condition: " + response);
                            if (Objects.equals(response, "success")) {
                                Toast.makeText(requireContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                int count = uploadCounter.incrementAndGet();
                                if (count == imageUrls.size()) {
                                    // All images have been uploaded
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(requireContext(), "Image/s are successfully uploaded ", Toast.LENGTH_SHORT).show();
                                            Log.d("MainActivity", "uploadImagesToServer - Number of uploaded images : " + count);
                                            clearForm();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                                Log.e("MainActivity", "uploadImagesToServer - Check upload.php");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(requireContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                    Log.d("Upload Image Method:", "ReportID on database: " + reportId);
                    return paramV;
                }
            };

            queue.add(stringRequest);
        }

        return success;
    }

    private void clearForm() {
        // Clear the imageContainer by removing all views
        descriptionEditText.setText("");
        barangayTextInputLayouts.setText("");
        imageUrls.clear();
        imageContainer.removeAllViews();
        requireActivity().finish();
        Log.d("MainActivity", "clearForm - Form Cleared");
    }

    // Rest of the code remains unchanged
    private void openImagePicker() {
        Log.d("MainActivity", "openImagePicker - has started");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    String imageUrl = imageUri.toString();
                    imageUrls.add(imageUrl);
                    Log.d("MainActivity", "onActivityResult - FIrst condition - Image URL: " + imageUrl); // Log the image URL
                }
            } else if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                // Process the imageUri and upload the image to your server
                // Obtain the URL of the uploaded image and add it to the imageUrls list
                // Example:
                String imageUrl = imageUri.toString();
                imageUrls.add(imageUrl);
                Log.d("MainActivity", "onActivityResult - Second condition - Image URL: " + imageUrl); // Log the image URL
            }
            // Display the selected images in the LinearLayout container
            displayImages();
        } else {
            // Log an error message if the result code or request code doesn't match
            Log.e("MainActivity", "onActivityResult - Failed to pick images. Result code: " + resultCode + ", Request code: " + requestCode);
        }
    }


    private void displayImages() {
        Log.d("MainActivity", "displayImages - has started");
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Clear the imageContainer before adding the new images
                imageContainer.removeAllViews();

                // Load and display the images from the imageUrls list
                for (final String imageUrl : imageUrls) {
                    // Create a new FrameLayout to hold the ImageView and delete button
                    FrameLayout imageLayout = new FrameLayout(requireActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(
                            getResources().getDimensionPixelSize(R.dimen.image_margin),
                            getResources().getDimensionPixelSize(R.dimen.image_margin),
                            getResources().getDimensionPixelSize(R.dimen.image_margin),
                            getResources().getDimensionPixelSize(R.dimen.image_margin)
                    );
                    imageLayout.setLayoutParams(layoutParams);

                    // Create a new ImageView for the image
                    ImageView imageView = new ImageView(requireActivity());
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
                    Button deleteButton = new Button(requireActivity());
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
                            // Remove the image URL from the list
                            imageUrls.remove(imageUrl);
                            // Redisplay the updated images
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
        });
    }

    // Add the following method to handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, request location updates
            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            // Permission denied, handle accordingly
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // The user has denied the permission but hasn't selected "Don't ask again"
                // You can show a dialog or message explaining why the permission is needed and request it again
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                // The user has denied the permission and selected "Don't ask again"
                // You can show a dialog or message informing the user that the permission is required and guide them to the app settings to enable it manually
                Toast.makeText(requireActivity(), "Location permission denied. Please enable it in the app settings.", Toast.LENGTH_SHORT).show();
            }
        }

    }
    // Method to navigate back to Step1Fragment
    private void navigateToStep1Fragment() {

        // Navigate to the previous fragment (Step1Fragment)
        ((createReport_activity) requireActivity()).navigateToPreviousFragment(new Step1Fragment());
    }
}
