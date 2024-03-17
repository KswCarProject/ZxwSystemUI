package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SingleShadeLockScreenOverScroller.kt */
public final class SingleShadeLockScreenOverScroller implements LockScreenShadeOverScroller {
    @NotNull
    public final Context context;
    public float expansionDragDownAmount;
    public int maxOverScrollAmount;
    @NotNull
    public final NotificationStackScrollLayoutController nsslController;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    public int totalDistanceForFullShadeTransition;

    /* compiled from: SingleShadeLockScreenOverScroller.kt */
    public interface Factory {
        @NotNull
        SingleShadeLockScreenOverScroller create(@NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController);
    }

    public SingleShadeLockScreenOverScroller(@NotNull ConfigurationController configurationController, @NotNull Context context2, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.context = context2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.nsslController = notificationStackScrollLayoutController;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ SingleShadeLockScreenOverScroller this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
            }
        });
    }

    public final void updateResources() {
        Resources resources = this.context.getResources();
        this.totalDistanceForFullShadeTransition = resources.getDimensionPixelSize(R$dimen.lockscreen_shade_qs_transition_distance);
        this.maxOverScrollAmount = resources.getDimensionPixelSize(R$dimen.lockscreen_shade_max_over_scroll_amount);
    }

    public float getExpansionDragDownAmount() {
        return this.expansionDragDownAmount;
    }

    public void setExpansionDragDownAmount(float f) {
        if (!(f == this.expansionDragDownAmount)) {
            this.expansionDragDownAmount = f;
            overScroll();
        }
    }

    public final void overScroll() {
        float f;
        if (this.statusBarStateController.getState() == 1) {
            float height = (float) this.nsslController.getHeight();
            f = Interpolators.getOvershootInterpolation(MathUtils.saturate(getExpansionDragDownAmount() / height), 0.6f, ((float) this.totalDistanceForFullShadeTransition) / height) * ((float) this.maxOverScrollAmount);
        } else {
            f = 0.0f;
        }
        this.nsslController.setOverScrollAmount((int) f);
    }
}
