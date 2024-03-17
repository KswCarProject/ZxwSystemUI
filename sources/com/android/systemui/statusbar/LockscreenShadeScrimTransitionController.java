package com.android.systemui.statusbar;

import android.content.Context;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeScrimTransitionController.kt */
public final class LockscreenShadeScrimTransitionController extends AbstractLockscreenShadeTransitionController {
    public float notificationsScrimDragAmount;
    public float notificationsScrimProgress;
    public int notificationsScrimTransitionDelay;
    public int notificationsScrimTransitionDistance;
    @NotNull
    public final ScrimController scrimController;
    public float scrimProgress;
    public int scrimTransitionDistance;

    public LockscreenShadeScrimTransitionController(@NotNull ScrimController scrimController2, @NotNull Context context, @NotNull ConfigurationController configurationController, @NotNull DumpManager dumpManager) {
        super(context, configurationController, dumpManager);
        this.scrimController = scrimController2;
    }

    public final float getScrimProgress() {
        return this.scrimProgress;
    }

    public final float getNotificationsScrimProgress() {
        return this.notificationsScrimProgress;
    }

    public final float getNotificationsScrimDragAmount() {
        return this.notificationsScrimDragAmount;
    }

    public void updateResources() {
        this.scrimTransitionDistance = getContext().getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_scrim_transition_distance);
        this.notificationsScrimTransitionDelay = getContext().getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_notifications_scrim_transition_delay);
        this.notificationsScrimTransitionDistance = getContext().getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_notifications_scrim_transition_distance);
    }

    public void onDragDownAmountChanged(float f) {
        this.scrimProgress = MathUtils.saturate(f / ((float) this.scrimTransitionDistance));
        float f2 = f - ((float) this.notificationsScrimTransitionDelay);
        this.notificationsScrimDragAmount = f2;
        float saturate = MathUtils.saturate(f2 / ((float) this.notificationsScrimTransitionDistance));
        this.notificationsScrimProgress = saturate;
        this.scrimController.setTransitionToFullShadeProgress(this.scrimProgress, saturate);
    }

    public void dump(@NotNull IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("LockscreenShadeScrimTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Resources:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("scrimTransitionDistance: ", Integer.valueOf(this.scrimTransitionDistance)));
        indentingPrintWriter.println(Intrinsics.stringPlus("notificationsScrimTransitionDelay: ", Integer.valueOf(this.notificationsScrimTransitionDelay)));
        indentingPrintWriter.println(Intrinsics.stringPlus("notificationsScrimTransitionDistance: ", Integer.valueOf(this.notificationsScrimTransitionDistance)));
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("State");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("dragDownAmount: ", Float.valueOf(getDragDownAmount())));
        indentingPrintWriter.println(Intrinsics.stringPlus("scrimProgress: ", Float.valueOf(getScrimProgress())));
        indentingPrintWriter.println(Intrinsics.stringPlus("notificationsScrimProgress: ", Float.valueOf(getNotificationsScrimProgress())));
        indentingPrintWriter.println(Intrinsics.stringPlus("notificationsScrimDragAmount: ", Float.valueOf(getNotificationsScrimDragAmount())));
    }
}
