package com.example.smartlight.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.models.Device;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Device> deviceList = null;

    public DeviceAdapter(Activity context, ArrayList<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return deviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.item_device, null, true);
        TextView deviceNameTv = view.findViewById(R.id.tv_device_name);
        deviceNameTv.setText(deviceList.get(i).getId() + ": " + Factory.types.get(deviceList.get(i).getType()).getName());

        return view;
    }
}
