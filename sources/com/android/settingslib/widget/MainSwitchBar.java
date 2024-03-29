package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.preference.R$styleable;
import com.android.settingslib.utils.BuildCompatUtils;
import java.util.ArrayList;
import java.util.List;

public class MainSwitchBar extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    public int mBackgroundActivatedColor;
    public int mBackgroundColor;
    public Drawable mBackgroundDisabled;
    public Drawable mBackgroundOff;
    public Drawable mBackgroundOn;
    public View mFrameView;
    public Switch mSwitch;
    public final List<OnMainSwitchChangeListener> mSwitchChangeListeners;
    public TextView mTextView;

    public MainSwitchBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public MainSwitchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MainSwitchBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public MainSwitchBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mSwitchChangeListeners = new ArrayList();
        LayoutInflater.from(context).inflate(R$layout.settingslib_main_switch_bar, this);
        if (!BuildCompatUtils.isAtLeastS()) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843829});
            this.mBackgroundActivatedColor = obtainStyledAttributes.getColor(0, 0);
            this.mBackgroundColor = context.getColor(R$color.material_grey_600);
            obtainStyledAttributes.recycle();
        }
        setFocusable(true);
        setClickable(true);
        this.mFrameView = findViewById(R$id.frame);
        this.mTextView = (TextView) findViewById(R$id.switch_text);
        this.mSwitch = (Switch) findViewById(16908352);
        if (BuildCompatUtils.isAtLeastS()) {
            this.mBackgroundOn = getContext().getDrawable(R$drawable.settingslib_switch_bar_bg_on);
            this.mBackgroundOff = getContext().getDrawable(R$drawable.settingslib_switch_bar_bg_off);
            this.mBackgroundDisabled = getContext().getDrawable(R$drawable.settingslib_switch_bar_bg_disabled);
        }
        addOnSwitchChangeListener(new MainSwitchBar$$ExternalSyntheticLambda0(this));
        if (this.mSwitch.getVisibility() == 0) {
            this.mSwitch.setOnCheckedChangeListener(this);
        }
        setChecked(this.mSwitch.isChecked());
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.Preference, 0, 0);
            setTitle(obtainStyledAttributes2.getText(R$styleable.Preference_android_title));
            obtainStyledAttributes2.recycle();
        }
        setBackground(this.mSwitch.isChecked());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Switch switchR, boolean z) {
        setChecked(z);
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        propagateChecked(z);
    }

    public boolean performClick() {
        this.mSwitch.performClick();
        return super.performClick();
    }

    public void setChecked(boolean z) {
        Switch switchR = this.mSwitch;
        if (switchR != null) {
            switchR.setChecked(z);
        }
        setBackground(z);
    }

    public boolean isChecked() {
        return this.mSwitch.isChecked();
    }

    public void setTitle(CharSequence charSequence) {
        TextView textView = this.mTextView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void show() {
        setVisibility(0);
        this.mSwitch.setOnCheckedChangeListener(this);
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    public void addOnSwitchChangeListener(OnMainSwitchChangeListener onMainSwitchChangeListener) {
        if (!this.mSwitchChangeListeners.contains(onMainSwitchChangeListener)) {
            this.mSwitchChangeListeners.add(onMainSwitchChangeListener);
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mTextView.setEnabled(z);
        this.mSwitch.setEnabled(z);
        if (!BuildCompatUtils.isAtLeastS()) {
            return;
        }
        if (z) {
            this.mFrameView.setBackground(isChecked() ? this.mBackgroundOn : this.mBackgroundOff);
        } else {
            this.mFrameView.setBackground(this.mBackgroundDisabled);
        }
    }

    public final void propagateChecked(boolean z) {
        setBackground(z);
        int size = this.mSwitchChangeListeners.size();
        for (int i = 0; i < size; i++) {
            this.mSwitchChangeListeners.get(i).onSwitchChanged(this.mSwitch, z);
        }
    }

    public final void setBackground(boolean z) {
        if (!BuildCompatUtils.isAtLeastS()) {
            setBackgroundColor(z ? this.mBackgroundActivatedColor : this.mBackgroundColor);
        } else {
            this.mFrameView.setBackground(z ? this.mBackgroundOn : this.mBackgroundOff);
        }
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean mChecked;
        public boolean mVisible;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mChecked = ((Boolean) parcel.readValue((ClassLoader) null)).booleanValue();
            this.mVisible = ((Boolean) parcel.readValue((ClassLoader) null)).booleanValue();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeValue(Boolean.valueOf(this.mChecked));
            parcel.writeValue(Boolean.valueOf(this.mVisible));
        }

        public String toString() {
            return "MainSwitchBar.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + this.mChecked + " visible=" + this.mVisible + "}";
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mChecked = this.mSwitch.isChecked();
        savedState.mVisible = isShowing();
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mSwitch.setChecked(savedState.mChecked);
        setChecked(savedState.mChecked);
        setBackground(savedState.mChecked);
        setVisibility(savedState.mVisible ? 0 : 8);
        this.mSwitch.setOnCheckedChangeListener(savedState.mVisible ? this : null);
        requestLayout();
    }
}
