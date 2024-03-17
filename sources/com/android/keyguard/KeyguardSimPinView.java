package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.core.graphics.drawable.DrawableCompat;
import com.android.systemui.R$id;

public class KeyguardSimPinView extends KeyguardPinBasedInputView {
    public ImageView mSimImageView;

    public int getPromptReasonStringRes(int i) {
        return 0;
    }

    public void startAppearAnimation() {
    }

    public KeyguardSimPinView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardSimPinView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setEsimLocked(boolean z, int i) {
        KeyguardEsimArea keyguardEsimArea = (KeyguardEsimArea) findViewById(R$id.keyguard_esim_area);
        keyguardEsimArea.setSubscriptionId(i);
        keyguardEsimArea.setVisibility(z ? 0 : 8);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        resetState();
    }

    public int getPasswordTextViewId() {
        return R$id.simPinEntry;
    }

    public void onFinishInflate() {
        this.mSimImageView = (ImageView) findViewById(R$id.keyguard_sim);
        super.onFinishInflate();
        View view = this.mEcaView;
        if (view instanceof EmergencyCarrierArea) {
            ((EmergencyCarrierArea) view).setCarrierTextVisible(true);
        }
    }

    public CharSequence getTitle() {
        return getContext().getString(17040524);
    }

    public void reloadColors() {
        super.reloadColors();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842808});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        DrawableCompat.setTint(DrawableCompat.wrap(this.mSimImageView.getDrawable()), color);
    }
}
