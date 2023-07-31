package com.system.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CrimeDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrimeDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimeDetailsFragment newInstance(String param1, String param2) {
        CrimeDetailsFragment fragment = new CrimeDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_details, container, false);

        // Get the crime type from the arguments
        if (getArguments() != null) {
            String crimeType = getArguments().getString("crimeType");
            if (crimeType != null) {
                // Now you have the selected crime type, you can use it as needed.
                // For example, you can display it in a TextView in the fragment.
                TextView crimeTypeTextView = view.findViewById(R.id.crimeTypeTextView);
                crimeTypeTextView.setText(crimeType);

                Button crimeTypeButton = view.findViewById(R.id.button);
                crimeTypeButton.setText(crimeType);

                Log.e("CrimeDetailsFragment", "onCreateView - Selected item: " + crimeType);
                Log.e("CrimeDetailsFragment", "onCreateView - crimeTypeTextView: " + crimeTypeTextView.getText());
                Log.e("CrimeDetailsFragment", "onCreateView - crimeTypeButton: " + crimeTypeButton.getText());
            } else {
                Log.e("CrimeDetailsFragment", "onCreateView - No selected item");
            }
        } else {
            Log.e("CrimeDetailsFragment", "onCreateView - arguments are null");
        }

        return view;
    }


}