package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.smartlight.R;
import com.example.smartlight.interfaces.MyFragment;

public class UserFragment extends Fragment implements MyFragment {

    private View view;
    private ImageButton userMenuBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initUI();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        userMenuBtn.setImageResource(R.drawable.ic_baseline_person_24);
    }

    private void initUI() {
        userMenuBtn = (ImageButton) getActivity().findViewById(R.id.btn_menu_user);
        userMenuBtn.setImageResource(R.drawable.ic_baseline_person_selected_24);
    }

    @Override
    public String getTAG() {
        return "User";
    }
}