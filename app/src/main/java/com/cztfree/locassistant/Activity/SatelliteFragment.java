package com.cztfree.locassistant.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cztfree.locassistant.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SatelliteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SatelliteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SatelliteFragment extends Fragment {

    public SatelliteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_satellite, container, false);
    }


}
