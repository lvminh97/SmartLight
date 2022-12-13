package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smartlight.Config;
import com.example.smartlight.R;
import com.example.smartlight.interfaces.MyFragment;

public class UserFragment extends Fragment implements MyFragment {

    private View view;
    private ImageButton userMenuBtn;
    private TextView fullnameTv, phoneTv, emailTv;

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

        fullnameTv = (TextView) view.findViewById(R.id.tv_username);
        emailTv = (TextView) view.findViewById(R.id.tv_email);
        phoneTv = (TextView) view.findViewById(R.id.tv_phone);

        fullnameTv.setText(Config.user.getFullname());
        emailTv.setText(Config.user.getEmail());
        phoneTv.setText(Config.user.getMobile());
    }

    @Override
    public String getTAG() {
        return "User";
    }
}