package com.android.systemui.statusbar.phone.shade.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.PrintWriter;
import kotlin.text.StringsKt__IndentKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SplitShadeOverScroller.kt */
public final class SplitShadeOverScroller implements ShadeOverScroller {
    @NotNull
    public final Context context;
    public float dragDownAmount;
    public int maxOverScrollAmount;
    @NotNull
    public final NotificationStackScrollLayoutController nsslController;
    public int panelState;
    public int previousOverscrollAmount;
    @NotNull
    public final QS qS;
    @Nullable
    public Animator releaseOverScrollAnimator;
    public long releaseOverScrollDuration;
    @NotNull
    public final ScrimController scrimController;

    /* compiled from: SplitShadeOverScroller.kt */
    public interface Factory {
        @NotNull
        SplitShadeOverScroller create(@NotNull QS qs, @NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController);
    }

    public final boolean shouldReleaseOverscroll(int i, int i2) {
        return i == 1 && i2 != 1;
    }

    public SplitShadeOverScroller(@NotNull ConfigurationController configurationController, @NotNull DumpManager dumpManager, @NotNull Context context2, @NotNull ScrimController scrimController2, @NotNull QS qs, @NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.context = context2;
        this.scrimController = scrimController2;
        this.qS = qs;
        this.nsslController = notificationStackScrollLayoutController;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ SplitShadeOverScroller this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        dumpManager.registerDumpable(new Dumpable() {
            public final void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
                SplitShadeOverScroller.this.dump(printWriter, strArr);
            }
        });
    }

    public final void updateResources() {
        Resources resources = this.context.getResources();
        this.maxOverScrollAmount = resources.getDimensionPixelSize(R$dimen.shade_max_over_scroll_amount);
        this.releaseOverScrollDuration = (long) resources.getInteger(R$integer.lockscreen_shade_over_scroll_release_duration);
    }

    public void onPanelStateChanged(int i) {
        if (shouldReleaseOverscroll(this.panelState, i)) {
            releaseOverScroll();
        }
        this.panelState = i;
    }

    public void onDragDownAmountChanged(float f) {
        if (!(this.dragDownAmount == f)) {
            this.dragDownAmount = f;
            if (shouldOverscroll()) {
                overScroll(f);
            }
        }
    }

    public final boolean shouldOverscroll() {
        return this.panelState == 1;
    }

    public final void overScroll(float f) {
        int calculateOverscrollAmount = calculateOverscrollAmount(f);
        applyOverscroll(calculateOverscrollAmount);
        this.previousOverscrollAmount = calculateOverscrollAmount;
    }

    public final int calculateOverscrollAmount(float f) {
        return (int) (MathUtils.saturate(f / ((float) this.nsslController.getHeight())) * ((float) this.maxOverScrollAmount));
    }

    public final void applyOverscroll(int i) {
        this.qS.setOverScrollAmount(i);
        this.scrimController.setNotificationsOverScrollAmount(i);
        this.nsslController.setOverScrollAmount(i);
    }

    public final void releaseOverScroll() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.previousOverscrollAmount, 0});
        ofInt.addUpdateListener(new SplitShadeOverScroller$releaseOverScroll$1(this));
        ofInt.setInterpolator(Interpolators.STANDARD);
        ofInt.setDuration(this.releaseOverScrollDuration);
        ofInt.start();
        this.releaseOverScrollAnimator = ofInt;
        this.previousOverscrollAmount = 0;
    }

    @VisibleForTesting
    public final void finishAnimations$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        Animator animator = this.releaseOverScrollAnimator;
        if (animator != null) {
            animator.end();
        }
        this.releaseOverScrollAnimator = null;
    }

    public final void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(StringsKt__IndentKt.trimIndent("\n            SplitShadeOverScroller:\n                Resources:\n                    releaseOverScrollDuration: " + this.releaseOverScrollDuration + "\n                    maxOverScrollAmount: " + this.maxOverScrollAmount + "\n                State:\n                    previousOverscrollAmount: " + this.previousOverscrollAmount + "\n                    dragDownAmount: " + this.dragDownAmount + "\n                    panelState: " + this.panelState + "\n            "));
    }
}
