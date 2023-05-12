package com.example.smartlight.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GestureDetectorCompat;

import com.example.smartlight.Factory;
import com.example.smartlight.R;

public class CustomizeRoomView extends CoordinatorLayout implements OnGestureListener {

    private View view;
    private GestureDetectorCompat gestureDetector;
    private CustomizeRoomListener listener;

    public CustomizeRoomView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomizeRoomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.view_customize_room, (ViewGroup) this, true);
        this.gestureDetector = new GestureDetectorCompat(context, (OnGestureListener)this);
    }

    public final CustomizeRoomListener getListener() {
        return this.listener;
    }

    public final void setListener(CustomizeRoomListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return this.gestureDetector.onTouchEvent(e) ? true : super.onTouchEvent(e);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        if(listener != null) {
            listener.onScroll(e2.getX(), e2.getY());
        }
        return true;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface CustomizeRoomListener {
        void onScroll(float x, float y);
    }
}
