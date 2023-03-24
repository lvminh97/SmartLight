package com.example.smartlight.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.interfaces.MyFragment;

public class SettingFragment extends Fragment implements MyFragment, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View view;
    private ImageButton settingMenuBtn;
    private Button introBtn;
    private Switch controlSw;

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

    private void initUI() {
        // rearrange the bottom menu
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        set.constrainPercentWidth(R.id.btn_add, 0.0f);
        set.constrainPercentWidth(R.id.btn_menu_home, 0.5f);
        set.constrainPercentWidth(R.id.btn_menu_user, 0.5f);
        set.applyTo((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        //
        introBtn = (Button) view.findViewById(R.id.btn_intro);
        introBtn.setOnClickListener(this);

        controlSw = (Switch) view.findViewById(R.id.sw_control);
        controlSw.setChecked(Factory.isControl);
        controlSw.setOnCheckedChangeListener(this);
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
        if(v.getId() == R.id.btn_intro) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("")
                    .setMessage("Smart Light\nVersion: " + Factory.version)
                    .show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.sw_control){
            Factory.isControl = isChecked;
        }
    }
}