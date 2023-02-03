package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.adapters.RoomAdapter;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Room;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyFragment, AdapterView.OnItemClickListener, View.OnClickListener {

    private View view;
    private GridView gridview;
    private Button settingBtn;
    private ImageButton homeMenuBtn;

    private RoomAdapter roomAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        homeMenuBtn.setImageResource(R.drawable.ic_baseline_home_24);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeMenuBtn.setImageResource(R.drawable.ic_baseline_home_selected_24);
    }

    private void initUI() {
        homeMenuBtn = (ImageButton) getActivity().findViewById(R.id.btn_menu_home);
        homeMenuBtn.setImageResource(R.drawable.ic_baseline_home_selected_24);

        gridview = (GridView) view.findViewById(R.id.grid_room);
        roomAdapter = new RoomAdapter(getActivity(), (ArrayList<Room>) Factory.roomList);
        gridview.setAdapter(roomAdapter);
        gridview.setOnItemClickListener(this);

        settingBtn = (Button) view.findViewById(R.id.btn_setting);
        settingBtn.setOnClickListener(this);
    }

    @Override
    public String getTAG() {
        return "Home";
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Factory.room = Factory.roomList.get(i);
        loadFragment(new ControlFragment());
    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_setting) {
            loadFragment(new SettingFragment());
        }
    }
}