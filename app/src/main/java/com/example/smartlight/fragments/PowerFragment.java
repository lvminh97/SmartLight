package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.interfaces.MyFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PowerFragment extends Fragment implements MyFragment, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private View view;
    private Button backBtn;
    private ImageButton lightningMenuBtn;
    private SeekBar powerSeek;
    private TextView powerTv;
    private BarChart powerGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_power, container, false);
        try {
            initUI();
        } catch (ParseException e) {}
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_24);
    }

    private void initUI() throws ParseException {
        lightningMenuBtn = getActivity().findViewById(R.id.btn_menu_lightning);
        lightningMenuBtn.setImageResource(R.drawable.ic_baseline_bolt_selected_24);

        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);

        powerSeek = (SeekBar) view.findViewById(R.id.seek_power);
        powerSeek.setOnSeekBarChangeListener(this);
        powerTv = (TextView) view.findViewById(R.id.tv_power);

        powerSeek.setProgress(20);
        powerTv.setText("20W");

        powerGraph = (BarChart) view.findViewById(R.id.graph_power);
        powerGraph.getDescription().setEnabled(false);
        powerGraph.getXAxis().setDrawGridLines(false);
        powerGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        powerGraph.setPinchZoom(false);
        powerGraph.setDoubleTapToZoomEnabled(false);
        powerGraph.getAxisRight().setEnabled(false);
        powerGraph.getLegend().setEnabled(false);

        List<String> day = new ArrayList<String>();
        day.add("15/11");
        day.add("16/11");
        day.add("17/11");
        day.add("18/11");
        day.add("19/11");
        day.add("20/11");

        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(0, 100));
        entries.add(new BarEntry(1, 120));
        entries.add(new BarEntry(2, 160));
        entries.add(new BarEntry(3, 140));
        entries.add(new BarEntry(4, 110));
        entries.add(new BarEntry(5, 130));

        BarDataSet dataSet = new BarDataSet(entries, "Lượng tiêu thụ điện");
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "$" + value;
            }
        });
        BarData barData = new BarData(dataSet);
        powerGraph.setData(barData);
        powerGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return day.get((int) value);
            }
        });
        powerGraph.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "$" + value;
            }
        });

        powerGraph.invalidate();
    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back) {
            loadFragment(new ControlFragment());
        }
    }

    @Override
    public String getTAG() {
        return "Power";
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int progress = seekBar.getProgress();
        powerTv.setText(progress + "W");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
    }
}
