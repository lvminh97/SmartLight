package com.example.smartlight.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.activities.SettingActivity;
import com.example.smartlight.adapters.RoomAdapter;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Room;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyFragment, AdapterView.OnItemClickListener, View.OnClickListener {

    private View view;
    private GridView gridview;
    private Button settingBtn;

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
        roomList.add(new Room(3, null, "Customize"));
        roomAdapter = new RoomAdapter(getActivity(), roomList);
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
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
    }
}