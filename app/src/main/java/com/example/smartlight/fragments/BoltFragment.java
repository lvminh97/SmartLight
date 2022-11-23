package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.smartlight.R;
import com.example.smartlight.interfaces.MyFragment;

public class BoltFragment extends Fragment implements MyFragment {

    private View view;

    public BoltFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bolt, container, false);
        return view;
    }

    @Override
    public String getTAG() {
        return "Bolt";
    }
}