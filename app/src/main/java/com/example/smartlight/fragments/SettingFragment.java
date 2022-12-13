package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.interfaces.MyFragment;

public class SettingFragment extends Fragment implements MyFragment, View.OnClickListener {

    private View view;
    private ImageButton settingMenuBtn;
    private Button userBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initUI();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        settingMenuBtn.setImageResource(R.drawable.ic_baseline_settings_24);
    }

    private void initUI() {
        settingMenuBtn = (ImageButton) getActivity().findViewById(R.id.btn_menu_setting);
        settingMenuBtn.setImageResource(R.drawable.ic_baseline_settings_selected_24);

        userBtn = view.findViewById(R.id.btn_account);
        userBtn.setOnClickListener(this);

    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public String getTAG() {
        return "Setting";
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_account) {
            loadFragment(new UserFragment());
        }
    }
}