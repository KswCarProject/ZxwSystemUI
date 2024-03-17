package com.android.systemui.statusbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.settingslib.WirelessUtils;
import com.android.systemui.statusbar.OperatorNameViewController;

public class OperatorNameView extends TextView {
    public boolean mDemoMode;

    public OperatorNameView(Context context) {
        this(context, (AttributeSet) null);
    }

    public OperatorNameView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OperatorNameView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setDemoMode(boolean z) {
        this.mDemoMode = z;
    }

    public void update(boolean z, boolean z2, OperatorNameViewController.SubInfo subInfo) {
        setVisibility(z ? 0 : 8);
        boolean isAirplaneModeOn = WirelessUtils.isAirplaneModeOn(this.mContext);
        if (!z2 || isAirplaneModeOn) {
            setText((CharSequence) null);
            setVisibility(8);
        } else if (!this.mDemoMode) {
            updateText(subInfo);
        }
    }

    public void updateText(OperatorNameViewController.SubInfo subInfo) {
        CharSequence charSequence = null;
        CharSequence carrierName = subInfo != null ? subInfo.getCarrierName() : null;
        if (!TextUtils.isEmpty(carrierName) && subInfo.simReady() && subInfo.stateInService()) {
            charSequence = carrierName;
        }
        setText(charSequence);
    }
}
