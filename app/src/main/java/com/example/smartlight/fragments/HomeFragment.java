package com.example.smartlight.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.smartlight.R;
import com.example.smartlight.adapters.RoomAdapter;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Room;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyFragment, GridView.OnItemSelectedListener {

    private View view;
    private GridView gridview;

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

    private void initUI() {
        gridview = (GridView) view.findViewById(R.id.grid_room);
        ArrayList<Room> roomList = new ArrayList<>();
        roomList.add(new Room(1, null, "Meeting Room"));
        roomList.add(new Room(2, null, "Classroom"));
        roomList.add(new Room(3, null, "Conference Room"));
        roomAdapter = new RoomAdapter(getActivity(), roomList);
        gridview.setAdapter(roomAdapter);
        gridview.setOnItemSelectedListener(this);
    }

    @Override
    public String getTAG() {
        return "Home";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}