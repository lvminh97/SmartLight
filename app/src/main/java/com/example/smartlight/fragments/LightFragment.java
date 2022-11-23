package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smartlight.R;
import com.example.smartlight.components.RotaryKnobView;
import com.example.smartlight.interfaces.MyFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class LightFragment extends Fragment implements MyFragment, RotaryKnobView.RotaryKnobListener {

    private View view;
    private RotaryKnobView lightKnob;
    private TextView lightTv;
    private LineChart lightGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        lightKnob = (RotaryKnobView) view.findViewById(R.id.knob_light);
        lightKnob.setListener(this);
        lightKnob.setValue(40);

        lightTv = (TextView) view.findViewById(R.id.tv_light);
        lightTv.setText("40%");

        lightGraph = (LineChart) view.findViewById(R.id.graph_light);
        lightGraph.getDescription().setEnabled(false);
        lightGraph.getXAxis().setDrawGridLines(false);
        lightGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lightGraph.setPinchZoom(false);
        lightGraph.setDoubleTapToZoomEnabled(false);
        lightGraph.getAxisRight().setEnabled(false);
        lightGraph.getLegend().setEnabled(false);
        
        List<String> times = new ArrayList<String>();
        times.add("21:30:00");
        times.add("21:30:30");
        times.add("21:31:00");
        times.add("21:31:30");
        times.add("21:32:00");
        times.add("21:32:30");
        times.add("21:33:00");
        times.add("21:33:30");
        times.add("21:34:00");
        times.add("21:34:30");

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 60));
        entries.add(new Entry(1, 62));
        entries.add(new Entry(2, 64));
        entries.add(new Entry(3, 63));
        entries.add(new Entry(4, 67));
        entries.add(new Entry(5, 50));
        entries.add(new Entry(6, 50));
        entries.add(new Entry(7, 51));
        entries.add(new Entry(8, 55));
        entries.add(new Entry(9, 70));

        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        lightGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return times.get((int) value);
            }
        });
        lightGraph.setData(lineData);
        lightGraph.invalidate();
    }

    @Override
    public void onRotate(int var1) {
        lightTv.setText(var1 + "%");
    }

    @Override
    public String getTAG() {
        return "Light";
    }
}
