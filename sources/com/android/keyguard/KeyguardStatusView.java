package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import androidx.core.graphics.ColorUtils;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.util.Set;

public class KeyguardStatusView extends GridLayout {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public KeyguardClockSwitch mClockView;
    public float mDarkAmount;
    public KeyguardSliceView mKeyguardSlice;
    public View mMediaHostContainer;
    public ViewGroup mStatusViewContainer;
    public int mTextColor;

    public KeyguardStatusView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDarkAmount = 0.0f;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mStatusViewContainer = (ViewGroup) findViewById(R$id.status_view_container);
        this.mClockView = (KeyguardClockSwitch) findViewById(R$id.keyguard_clock_container);
        if (KeyguardClockAccessibilityDelegate.isNeeded(this.mContext)) {
            this.mClockView.setAccessibilityDelegate(new KeyguardClockAccessibilityDelegate(this.mContext));
        }
        this.mKeyguardSlice = (KeyguardSliceView) findViewById(R$id.keyguard_slice_view);
        this.mTextColor = this.mClockView.getCurrentTextColor();
        this.mMediaHostContainer = findViewById(R$id.status_view_media_container);
        updateDark();
    }

    public void setDarkAmount(float f) {
        if (this.mDarkAmount != f) {
            this.mDarkAmount = f;
            this.mClockView.setDarkAmount(f);
            CrossFadeHelper.fadeOut(this.mMediaHostContainer, f);
            updateDark();
        }
    }

    public void updateDark() {
        int blendARGB = ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
        this.mKeyguardSlice.setDarkAmount(this.mDarkAmount);
        this.mClockView.setTextColor(blendARGB);
    }

    public void setChildrenTranslationYExcludingMediaView(float f) {
        setChildrenTranslationYExcluding(f, Set.of(this.mMediaHostContainer));
    }

    public final void setChildrenTranslationYExcluding(float f, Set<View> set) {
        for (int i = 0; i < this.mStatusViewContainer.getChildCount(); i++) {
            View childAt = this.mStatusViewContainer.getChildAt(i);
            if (!set.contains(childAt)) {
                childAt.setTranslationY(f);
            }
        }
    }
}
