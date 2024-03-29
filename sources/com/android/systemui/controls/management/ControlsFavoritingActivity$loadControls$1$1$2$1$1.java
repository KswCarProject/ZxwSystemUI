package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.R$string;
import com.android.systemui.controls.TooltipManager;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$loadControls$1$1$2$1$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    public ControlsFavoritingActivity$loadControls$1$1$2$1$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        ManagementPageIndicator access$getPageIndicator$p = this.this$0.pageIndicator;
        ManagementPageIndicator managementPageIndicator = null;
        if (access$getPageIndicator$p == null) {
            access$getPageIndicator$p = null;
        }
        if (access$getPageIndicator$p.getVisibility() == 0 && this.this$0.mTooltipManager != null) {
            int[] iArr = new int[2];
            ManagementPageIndicator access$getPageIndicator$p2 = this.this$0.pageIndicator;
            if (access$getPageIndicator$p2 == null) {
                access$getPageIndicator$p2 = null;
            }
            access$getPageIndicator$p2.getLocationOnScreen(iArr);
            int i = iArr[0];
            ManagementPageIndicator access$getPageIndicator$p3 = this.this$0.pageIndicator;
            if (access$getPageIndicator$p3 == null) {
                access$getPageIndicator$p3 = null;
            }
            int width = i + (access$getPageIndicator$p3.getWidth() / 2);
            int i2 = iArr[1];
            ManagementPageIndicator access$getPageIndicator$p4 = this.this$0.pageIndicator;
            if (access$getPageIndicator$p4 != null) {
                managementPageIndicator = access$getPageIndicator$p4;
            }
            int height = i2 + managementPageIndicator.getHeight();
            TooltipManager access$getMTooltipManager$p = this.this$0.mTooltipManager;
            if (access$getMTooltipManager$p != null) {
                access$getMTooltipManager$p.show(R$string.controls_structure_tooltip, width, height);
            }
        }
    }
}
