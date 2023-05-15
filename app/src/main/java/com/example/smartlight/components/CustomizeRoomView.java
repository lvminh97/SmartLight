package com.example.smartlight.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GestureDetectorCompat;

import com.example.smartlight.Factory;
import com.example.smartlight.R;

public class CustomizeRoomView extends CoordinatorLayout implements OnGestureListener {

    private View view;
    private CoordinatorLayout roomLayout;
    private ImageView hintImg;
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
        roomLayout = view.findViewById(R.id.layout_customize_room);
        hintImg = (ImageView) view.findViewById(R.id.img_hint);
        this.gestureDetector = new GestureDetectorCompat(context, (OnGestureListener)this);
    }

    public CoordinatorLayout getRoomLayout() {
        return roomLayout;
    }

    public final CustomizeRoomListener getListener() {
        return this.listener;
    }

    public final void setListener(CustomizeRoomListener listener) {
        this.listener = listener;
    }

    public void showHint(boolean show) {
        if(show){
            hintImg.setVisibility(View.VISIBLE);
        }
        else{
            hintImg.setVisibility(View.INVISIBLE);
        }
    }

    public void setHintPosition(int x, int y) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) hintImg.getLayoutParams();
        if(layoutParams != null) {
            layoutParams.leftMargin = x;
            layoutParams.topMargin = y;
            hintImg.setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(listener != null) {
            listener.onTouch(e);
        }
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
        if(listener != null) {
            listener.onSingleTapUp();
        }
        return true;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        if(listener != null) {
            listener.onScroll(distanceX, distanceY);
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
        void onSingleTapUp();
        void onTouch(MotionEvent e);
    }
}
