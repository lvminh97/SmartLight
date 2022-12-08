package com.example.smartlight.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.components.RotaryKnobView;
import com.example.smartlight.interfaces.MyFragment;

public class ControlFragment extends Fragment implements MyFragment, View.OnClickListener, RotaryKnobView.RotaryKnobListener {

    private View view;
    private RotaryKnobView tempKnob;
    private TextView tempTv;
    private Button lightBtn, powerBtn, setupBtn;
    private ImageButton lightningMenuBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control, container, false);
        initUI();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_24);
    }

    private void initUI() {
        lightningMenuBtn = getActivity().findViewById(R.id.btn_menu_lightning);
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);
        tempKnob = (RotaryKnobView) view.findViewById(R.id.knob);
        tempKnob.setListener(this);
        tempKnob.setValue(30);
        tempTv = (TextView) view.findViewById(R.id.tv_temp);
        tempTv.setText("30 oC");
        lightBtn = (Button) view.findViewById(R.id.btn_light);
        lightBtn.setOnClickListener(this);
        powerBtn = (Button) view.findViewById(R.id.btn_power);
        powerBtn.setOnClickListener(this);
        setupBtn = (Button) view.findViewById(R.id.btn_setup);
        setupBtn.setOnClickListener(this);
    }

    @Override
    public String getTAG() {
        return "Control";
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_light) {
            loadFragment(new LightFragment());
        }
        else if(view.getId() == R.id.btn_power){
            loadFragment(new PowerFragment());
        }
        else if(view.getId() == R.id.btn_setup){
            new AlertDialog.Builder(getContext())
                    .setTitle("Thông báo")
                    .setMessage("Cài đặt các thông số mặc định cho hệ thống thành công!")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    public void onRotate(int value) {
        tempTv.setText(value + " oC");
    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
