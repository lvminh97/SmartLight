package com.example.smartlight.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.models.Room;

import java.util.ArrayList;

public class RoomAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Room> roomList;

    public RoomAdapter(Activity context, ArrayList<Room> list) {
        this.context = context;
        this.roomList = list;
    }

    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int i) {
        return roomList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View gridView = inflater.inflate(R.layout.item_room, null, true);

        ImageView img = gridView.findViewById(R.id.img_room);
        img.setImageResource(R.drawable.building);
        if(Factory.displayMetrics.heightPixels < 1600) {
            img.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }

        TextView name = gridView.findViewById(R.id.tv_room);
        if(Factory.displayMetrics.heightPixels < 1600) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) name.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            name.setLayoutParams(layoutParams);
        }
        name.setText(roomList.get(i).getName());

        return gridView;
    }
}
