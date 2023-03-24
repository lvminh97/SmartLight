package com.example.smartlight.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import androidx.core.view.GestureDetectorCompat;

import com.example.smartlight.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RotaryKnobView extends RelativeLayout implements OnGestureListener {
    private View view;
    private GestureDetectorCompat gestureDetector;
    private int maxValue;
    private int minValue;
    @Nullable
    private RotaryKnobView.RotaryKnobListener listener;
    private int value;
    private ImageView imgView;
    private Drawable knobDrawable;
    private float divider;
    private float prevDegree;
    private boolean enable = true;
    private boolean lock = false;

    public RotaryKnobView(@NotNull Context context) {
        super(context);
        init(context, null);
    }

    public RotaryKnobView(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RotaryKnobView(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NotNull Context context, @Nullable AttributeSet attrs) {
        this.maxValue = 100;
        this.value = 0;
        this.divider = 300.0F / (float)(this.maxValue - this.minValue);

        view = LayoutInflater.from(context).inflate(R.layout.rotary_knob_view, (ViewGroup)this, true);
        imgView = (ImageView)this.findViewById(R.id.knobImageView);
        imgView.setScaleType(ScaleType.MATRIX);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RotaryKnobView, 0, 0);
        try {
            this.minValue = typedArray.getInt(R.styleable.RotaryKnobView_minValue, 0);
            this.maxValue = typedArray.getInt(R.styleable.RotaryKnobView_maxValue, 100) + 1;
            this.divider = 300.0F / (float)(this.maxValue - this.minValue);
            this.value = typedArray.getInt(R.styleable.RotaryKnobView_initialValue, 0);
            this.knobDrawable = typedArray.getDrawable(R.styleable.RotaryKnobView_knobDrawable);
            ((ImageView)this.findViewById (R.id.knobImageView)).setImageDrawable(this.knobDrawable);
            this.setKnobPosition ((this.value - this.minValue) * this.divider - 150);
        } finally {
            typedArray.recycle();
        }

        this.gestureDetector = new GestureDetectorCompat(context, (OnGestureListener)this);
    }

    @Nullable
    public final RotaryKnobView.RotaryKnobListener getListener() {
        return this.listener;
    }

    public final void setListener(@Nullable RotaryKnobView.RotaryKnobListener listener) {
        this.listener = listener;
    }

    public final int getValue() {
        return this.value;
    }

    public final void setValue(int value) {
        this.value = value;
        float deg = (this.value - this.minValue) * this.divider - 150;
        this.setKnobPosition (deg);
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean onScroll(@NotNull MotionEvent e1, @NotNull MotionEvent e2, float distanceX, float distanceY) {
        if(lock == true)
            return false;
//        float startDegress = this.calculateAngle(e1.getX(), e1.getY());
        float rotationDegrees = this.calculateAngle(e2.getX(), e2.getY());
        if (rotationDegrees <= 310 || rotationDegrees >= 350) {
            if(rotationDegrees <= 300) {
                if (enable == false) {
                    return false;
                }
                int tmpValue = (int) (rotationDegrees / this.divider + (float) this.minValue);
                this.value = tmpValue;
                this.setKnobPosition(rotationDegrees - 150);
                if (this.listener != null) {
                    RotaryKnobView.RotaryKnobListener listener = this.listener;
                    listener.onRotate(this.value);
                }
            }
        }
        else {
            enable = false;
        }

        return true;
    }

    private final float calculateAngle(float x, float y) {
        double px = (double)(x / (float)view.getWidth()) - 0.5D;
        double py = (double)((float)1 - y / (float)view.getHeight()) - 0.5D;
        float angle = -((float)Math.toDegrees(Math.atan2(py, px))) + 90;
        if (angle > 180) {
            angle -= 360;
        }
        if(angle >= -150) {
            angle += 150;
        }
        else{
            angle += 510;
        }

        return angle;
    }

    private final void setKnobPosition(float deg) {
        Matrix matrix = new Matrix();
        float w, h;
        w = (getContext().getResources().getDimension(R.dimen.knob_width));
        h = (getContext().getResources().getDimension(R.dimen.knob_height));
        matrix.postRotate(deg, w / 2.0f, h / 2.0f);
        imgView.setImageMatrix(matrix);
    }

    public boolean onTouchEvent(@NotNull MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN){
            enable = true;
        }
        if(this.listener != null){
            RotaryKnobView.RotaryKnobListener listener = this.listener;
            listener.onTouch(event);
        }
        return this.gestureDetector.onTouchEvent(event) ? true : super.onTouchEvent(event);
    }

    public boolean onDown(@NotNull MotionEvent event) {
        return true;
    }

    public boolean onSingleTapUp(@NotNull MotionEvent e) {
        return false;
    }

    public boolean onFling(@NotNull MotionEvent arg0, @NotNull MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    public void onLongPress(@NotNull MotionEvent e) {
    }

    public void onShowPress(@NotNull MotionEvent e) {
    }

    public interface RotaryKnobListener {
        void onRotate(int var1);
        void onTouch(@NotNull MotionEvent e);
    }
}
