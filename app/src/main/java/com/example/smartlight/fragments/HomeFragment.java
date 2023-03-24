package com.example.smartlight.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.activities.LoginActivity;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.adapters.RoomAdapter;
import com.example.smartlight.interfaces.MyFragment;
import com.example.smartlight.models.Room;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyFragment, AdapterView.OnItemClickListener, View.OnClickListener {

    private View view;
    private GridView gridview;
    private Button settingBtn, logoutBtn;
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
        Factory.device = null;
        Factory.deviceList = null;
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

        gridview = (GridView) view.findViewById(R.id.grid_room);
        roomAdapter = new RoomAdapter(getActivity(), (ArrayList<Room>) Factory.roomList);
        gridview.setAdapter(roomAdapter);
        gridview.setOnItemClickListener(this);

        settingBtn = (Button) view.findViewById(R.id.btn_setting);
        settingBtn.setOnClickListener(this);
        logoutBtn = (Button) view.findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public String getTAG() {
        return "Home";
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(Factory.roomList.get(i).getId() != 0) {
            Factory.room = Factory.roomList.get(i);
            loadFragment(new ControlFragment());
        }
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
        else if(view.getId() == R.id.btn_logout){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Thông báo")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = getActivity().getSharedPreferences("SMARTLIGHT", MODE_PRIVATE);
                            prefs.edit().putStringSet("password", null).commit();
                            Factory.user = null;
                            MainActivity.FRAG_ID = "";
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Bỏ qua", null)
                    .show();
        }
    }
}