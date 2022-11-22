package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smartlight.R;
import com.example.smartlight.adapters.RoomAdapter;
import com.example.smartlight.components.RotaryKnobView;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Room;

import java.util.ArrayList;

public class ControlFragment extends Fragment implements MyFragment, RotaryKnobView.RotaryKnobListener {

    private View view;
    RotaryKnobView tempKnob;
    TextView tempTv;

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

    private void initUI() {
        tempKnob = (RotaryKnobView) view.findViewById(R.id.knob);
        tempKnob.setListener(this);
        tempTv = (TextView) view.findViewById(R.id.tv_temp);
    }

    @Override
    public String getTAG() {
        return "Control";
    }

    @Override
    public void onRotate(int value) {
        tempTv.setText(value + "oC");
    }
}
