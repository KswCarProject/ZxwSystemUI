package com.android.systemui.statusbar.phone;

import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$style;
import com.android.systemui.statusbar.policy.ConfigurationController;

/* compiled from: LargeScreenShadeHeaderController.kt */
public final class LargeScreenShadeHeaderController$bindConfigurationListener$listener$1 implements ConfigurationController.ConfigurationListener {
    public final /* synthetic */ LargeScreenShadeHeaderController this$0;

    public LargeScreenShadeHeaderController$bindConfigurationListener$listener$1(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.this$0 = largeScreenShadeHeaderController;
    }

    public void onDensityOrFontScaleChanged() {
        int i = R$style.TextAppearance_QS_Status_Big;
        int i2 = R$style.TextAppearance_QS_Status_Mid;
        int i3 = R$style.TextAppearance_QS_Status;
        FontSizeUtils.updateFontSizeFromStyle(this.this$0.clock, i);
        FontSizeUtils.updateFontSizeFromStyle(this.this$0.date, i2);
        this.this$0.qsCarrierGroup.updateTextAppearance(i3);
    }
}
