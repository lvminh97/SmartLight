package com.example.smartlight.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.models.Device;
import com.example.smartlight.models.Room;

public class CustomizeDeviceButton extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {

    private View view;
    private int width = 50, height = 50;
    private int curX, curY;
    private Button button;
    private Device device;
    public CustomizeDeviceButtonListener listener;

    public CustomizeDeviceButton(Context context) {
        super(context);
        initUI(context, null);
    }

    public CustomizeDeviceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context, attrs);
    }

    private void initUI(@NonNull Context context, @Nullable AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.button_customize, (ViewGroup) this, true);
        button = (Button) view.findViewById(R.id.btn_customize_device);
        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(layoutParams == null) {
            layoutParams = new LayoutParams(width, height);
        }
        else {
            layoutParams.width = width;
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    public int[] getPosition() {
        int[] res = {0, 0};
        ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        res[0] = marginLayoutParams.leftMargin;
        res[1] = marginLayoutParams.topMargin;
        return res;
    }

    public void setPosition(int x, int y) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        curX = Math.round((float) x / width);
        curY = Math.round((float) y / height);
        marginLayoutParams.setMargins(x, y, 0, 0);
        view.setLayoutParams(marginLayoutParams);
    }

    public void setImage(int imageId) {
        button.setBackgroundResource(imageId);
    }

    public void setActive(boolean active) {
        if(active){
            view.setBackgroundResource(R.drawable.bg_customize_device);
        }
        else{
            view.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return this.device;
    }

    public int getCurX() {
        return curX;
    }

    public void setCurX(int curX) {
        this.curX = curX;
    }

    public int getCurY() {
        return curY;
    }

    public void setCurY(int curY) {
        this.curY = curY;
    }

    public CustomizeDeviceButtonListener getListener() {
        return listener;
    }

    public void setListener(CustomizeDeviceButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null) {
            listener.onTap(this);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(listener != null) {
            listener.onHold(this);
        }
        return true;
    }

    public interface CustomizeDeviceButtonListener {
        public void onTap(CustomizeDeviceButton button);
        public void onHold(CustomizeDeviceButton button);
    }
}
