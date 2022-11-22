package com.example.smartlight.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import androidx.core.view.GestureDetectorCompat;
import com.example.smartlight.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RotaryKnobView extends RelativeLayout implements OnGestureListener {
    private GestureDetectorCompat gestureDetector;
    private int maxValue;
    private int minValue;
    @Nullable
    private RotaryKnobView.RotaryKnobListener listener;
    private int value;
    private Drawable knobDrawable;
    private float divider;

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
        this.maxValue = 99;
        this.value = 130;
        this.divider = 300.0F / (float)(this.maxValue - this.minValue);
        ++this.maxValue;

        LayoutInflater.from(context).inflate(R.layout.rotary_knob_view, (ViewGroup)this, true);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RotaryKnobView, 0, 0);
        TypedArray $this$apply = typedArray;

        try {
            this.minValue = typedArray.getInt(R.styleable.RotaryKnobView_minValue, 0);
            this.maxValue = typedArray.getInt(R.styleable.RotaryKnobView_maxValue, 100) + 1;
            this.divider = 300.0F / (float)(this.maxValue - this.minValue);
            this.value = typedArray.getInt(R.styleable.RotaryKnobView_initialValue, 50);
            this.knobDrawable = typedArray.getDrawable(R.styleable.RotaryKnobView_knobDrawable);
            ((ImageView)this.findViewById (R.id.knobImageView)).setImageDrawable(this.knobDrawable);
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

    public final void setValue(int var1) {
        this.value = var1;
    }

    public boolean onScroll(@NotNull MotionEvent e1, @NotNull MotionEvent e2, float distanceX, float distanceY) {
        float rotationDegrees = this.calculateAngle(e2.getX(), e2.getY());
        if (rotationDegrees >= -150 && rotationDegrees <= 150) {
            this.setKnobPosition(rotationDegrees);
            float valueRangeDegrees = rotationDegrees + 150;
            this.value = (int)(valueRangeDegrees / this.divider + (float)this.minValue);
            if (this.listener != null) {
                RotaryKnobView.RotaryKnobListener listener = this.listener;
                listener.onRotate(this.value);
            }
        }

        return true;
    }

    private final float calculateAngle(float x, float y) {
        double px = (double)(x / (float)this.getWidth()) - 0.5D;
        double py = (double)((float)1 - y / (float)this.getHeight()) - 0.5D;
        float angle = -((float)Math.toDegrees(Math.atan2(py, px))) + 90;
        if (angle > 180) {
            angle -= 360;
        }

        return angle;
    }

    private final void setKnobPosition(float deg) {
        Matrix matrix = new Matrix();
        ImageView imgView = (ImageView)this.findViewById(R.id.knobImageView);
        imgView.setScaleType(ScaleType.MATRIX);
        matrix.postRotate(deg, this.getWidth() / 2, this.getHeight() / 2);
        imgView = (ImageView)this.findViewById(R.id.knobImageView);
        imgView.setImageMatrix(matrix);
    }

    public boolean onTouchEvent(@NotNull MotionEvent event) {
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
    }
}
