package com.android.keyguard.clock;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.doze.util.BurnInHelperKt;

public class ClockLayout extends FrameLayout {
    public View mAnalogClock;
    public int mBurnInPreventionOffsetX;
    public int mBurnInPreventionOffsetY;
    public float mDarkAmount;

    public ClockLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ClockLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ClockLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mAnalogClock = findViewById(R$id.analog_clock);
        Resources resources = getResources();
        this.mBurnInPreventionOffsetX = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_x);
        this.mBurnInPreventionOffsetY = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_y);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        positionChildren();
    }

    public final void positionChildren() {
        float lerp = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mBurnInPreventionOffsetX * 2, true) - this.mBurnInPreventionOffsetX), this.mDarkAmount);
        float lerp2 = MathUtils.lerp(0.0f, ((float) BurnInHelperKt.getBurnInOffset(this.mBurnInPreventionOffsetY * 2, false)) - (((float) this.mBurnInPreventionOffsetY) * 0.5f), this.mDarkAmount);
        View view = this.mAnalogClock;
        if (view != null) {
            view.setX(Math.max(0.0f, ((float) (getWidth() - this.mAnalogClock.getWidth())) * 0.5f) + (lerp * 3.0f));
            this.mAnalogClock.setY(Math.max(0.0f, ((float) (getHeight() - this.mAnalogClock.getHeight())) * 0.5f) + (lerp2 * 3.0f));
        }
    }
}
