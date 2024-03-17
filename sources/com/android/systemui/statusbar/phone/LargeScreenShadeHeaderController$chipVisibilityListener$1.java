package com.android.systemui.statusbar.phone;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.R$id;
import com.android.systemui.qs.ChipVisibilityListener;

/* compiled from: LargeScreenShadeHeaderController.kt */
public final class LargeScreenShadeHeaderController$chipVisibilityListener$1 implements ChipVisibilityListener {
    public final /* synthetic */ LargeScreenShadeHeaderController this$0;

    public LargeScreenShadeHeaderController$chipVisibilityListener$1(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.this$0 = largeScreenShadeHeaderController;
    }

    public void onChipVisibilityRefreshed(boolean z) {
        if (this.this$0.header instanceof MotionLayout) {
            ConstraintSet constraintSet = ((MotionLayout) this.this$0.header).getConstraintSet(LargeScreenShadeHeaderController.QQS_HEADER_CONSTRAINT);
            float f = 0.0f;
            constraintSet.setAlpha(R$id.statusIcons, z ? 0.0f : 1.0f);
            int i = R$id.batteryRemainingIcon;
            if (!z) {
                f = 1.0f;
            }
            constraintSet.setAlpha(i, f);
            ((MotionLayout) this.this$0.header).updateState(LargeScreenShadeHeaderController.QQS_HEADER_CONSTRAINT, constraintSet);
        }
    }
}
