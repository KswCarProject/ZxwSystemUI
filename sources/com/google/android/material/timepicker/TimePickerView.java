package com.google.android.material.timepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;

class TimePickerView extends ConstraintLayout {
    public final ClockFaceView clockFace;
    public final ClockHandView clockHandView;
    public final Chip hourView;
    public final Chip minuteView;
    public final View.OnClickListener selectionListener;
    public final MaterialButtonToggleGroup toggle;

    public interface OnDoubleTapListener {
        void onDoubleTap();
    }

    public interface OnPeriodChangeListener {
        void onPeriodChange(int i);
    }

    public interface OnSelectionChange {
        void onSelectionChanged(int i);
    }

    public TimePickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TimePickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TimePickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.selectionListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (TimePickerView.this.getClass() != null) {
                    TimePickerView.this.getClass().onSelectionChanged(((Integer) view.getTag(R$id.selection_type)).intValue());
                }
            }
        };
        LayoutInflater.from(context).inflate(R$layout.material_timepicker, this);
        this.clockFace = (ClockFaceView) findViewById(R$id.material_clock_face);
        MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup) findViewById(R$id.material_clock_period_toggle);
        this.toggle = materialButtonToggleGroup;
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            public void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean z) {
                int i2 = i == R$id.material_clock_period_pm_button ? 1 : 0;
                if (TimePickerView.this.getClass() != null && z) {
                    TimePickerView.this.getClass().onPeriodChange(i2);
                }
            }
        });
        Chip chip = (Chip) findViewById(R$id.material_minute_tv);
        this.minuteView = chip;
        Chip chip2 = (Chip) findViewById(R$id.material_hour_tv);
        this.hourView = chip2;
        this.clockHandView = (ClockHandView) findViewById(R$id.material_clock_hand);
        ViewCompat.setAccessibilityLiveRegion(chip, 2);
        ViewCompat.setAccessibilityLiveRegion(chip2, 2);
        setupDoubleTap();
        setUpDisplay();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void setupDoubleTap() {
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent motionEvent) {
                OnDoubleTapListener access$200 = TimePickerView.this.getClass();
                if (access$200 == null) {
                    return false;
                }
                access$200.onDoubleTap();
                return true;
            }
        });
        AnonymousClass4 r1 = new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (((Checkable) view).isChecked()) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
                return false;
            }
        };
        this.minuteView.setOnTouchListener(r1);
        this.hourView.setOnTouchListener(r1);
    }

    public final void setUpDisplay() {
        Chip chip = this.minuteView;
        int i = R$id.selection_type;
        chip.setTag(i, 12);
        this.hourView.setTag(i, 10);
        this.minuteView.setOnClickListener(this.selectionListener);
        this.hourView.setOnClickListener(this.selectionListener);
    }

    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (view == this && i == 0) {
            updateToggleConstraints();
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateToggleConstraints();
    }

    public final void updateToggleConstraints() {
        if (this.toggle.getVisibility() == 0) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) this);
            int i = 1;
            if (ViewCompat.getLayoutDirection(this) == 0) {
                i = 2;
            }
            constraintSet.clear(R$id.material_clock_display, i);
            constraintSet.applyTo(this);
        }
    }
}
