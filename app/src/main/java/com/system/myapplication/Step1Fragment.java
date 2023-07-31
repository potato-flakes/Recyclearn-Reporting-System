package com.system.myapplication;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Step1Fragment extends Fragment {

    private RadioGroup crimeRadioGroup;
    private String selectedCrimeType;
    private OnStepCompletionListener stepCompletionListener;
    private int currentPage;

    public Step1Fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);
        // Find the RadioGroup and set the OnCheckedChangeListener
        crimeRadioGroup = view.findViewById(R.id.crimeRadioGroup);
        crimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected RadioButton's text
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                selectedCrimeType = selectedRadioButton.getText().toString();
            }
        });


        // Get the value from the EditText and pass it to Step2Fragment when the "Next" button is clicked
        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = selectedCrimeType;
                // Create a Bundle to hold the value
                Bundle bundle = new Bundle();
                bundle.putString("selectedCrimeType", value);

                // Create an instance of Step2Fragment and set the arguments
                Step2Fragment step2Fragment = new Step2Fragment();
                step2Fragment.setArguments(bundle);

                // Set Step1Fragment as the target fragment for Step2Fragment
                step2Fragment.setTargetFragment(Step1Fragment.this, 1);

                // Navigate to Step2Fragment
                ((createReport_activity) requireActivity()).navigateToNextFragment(step2Fragment);
            }
        });

        return view;
    }

    // Method to receive the selected crime type from Step2Fragment
    public void setSelectedCrimeType(String selectedCrimeType) {
        this.selectedCrimeType = selectedCrimeType;
        // Set the selected crime type to the appropriate RadioButton in the crimeRadioGroup
        for (int i = 0; i < crimeRadioGroup.getChildCount(); i++) {
            View view = crimeRadioGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(selectedCrimeType)) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }
    }
    // Static method to create a new instance of the fragment and pass the currentPage value
    public static Step1Fragment newInstance(int currentPage) {
        Step1Fragment fragment = new Step1Fragment();
        Bundle args = new Bundle();
        args.putInt("currentPage", currentPage);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure that the hosting activity implements the OnStepCompletionListener
        if (context instanceof OnStepCompletionListener) {
            stepCompletionListener = (OnStepCompletionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnStepCompletionListener");
        }

        // Retrieve the currentPage value from the arguments
        if (getArguments() != null) {
            currentPage = getArguments().getInt("currentPage", 1);
        }
    }

    // After the user has completed the input (e.g., when clicking "Next")
    private void onStepCompleted() {
        stepCompletionListener.onStepCompleted(currentPage, true); // currentPage should be the current step number
    }

    // When the user wants to go back (e.g., when clicking "Back")
    private void onGoBack() {
        stepCompletionListener.onStepCompleted(currentPage, false);
    }

}
