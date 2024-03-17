package com.android.systemui.statusbar;

import android.content.Context;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeKeyguardTransitionController.kt */
public final class LockscreenShadeKeyguardTransitionController extends AbstractLockscreenShadeTransitionController {
    public float alpha;
    public float alphaProgress;
    public int alphaTransitionDistance;
    public int keyguardTransitionDistance;
    public int keyguardTransitionOffset;
    @NotNull
    public final MediaHierarchyManager mediaHierarchyManager;
    @NotNull
    public final NotificationPanelViewController notificationPanelController;
    public float statusBarAlpha;
    public int translationY;
    public float translationYProgress;

    /* compiled from: LockscreenShadeKeyguardTransitionController.kt */
    public interface Factory {
        @NotNull
        LockscreenShadeKeyguardTransitionController create(@NotNull NotificationPanelViewController notificationPanelViewController);
    }

    public LockscreenShadeKeyguardTransitionController(@NotNull MediaHierarchyManager mediaHierarchyManager2, @NotNull NotificationPanelViewController notificationPanelViewController, @NotNull Context context, @NotNull ConfigurationController configurationController, @NotNull DumpManager dumpManager) {
        super(context, configurationController, dumpManager);
        this.mediaHierarchyManager = mediaHierarchyManager2;
        this.notificationPanelController = notificationPanelViewController;
    }

    public void updateResources() {
        this.alphaTransitionDistance = getContext().getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_npvc_keyguard_content_alpha_transition_distance);
        this.keyguardTransitionDistance = getContext().getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_keyguard_transition_distance);
        this.keyguardTransitionOffset = getContext().getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_keyguard_transition_vertical_offset);
    }

    public void onDragDownAmountChanged(float f) {
        float saturate = MathUtils.saturate(f / ((float) this.alphaTransitionDistance));
        this.alphaProgress = saturate;
        this.alpha = 1.0f - saturate;
        int calculateKeyguardTranslationY = calculateKeyguardTranslationY(f);
        this.translationY = calculateKeyguardTranslationY;
        this.notificationPanelController.setKeyguardTransitionProgress(this.alpha, calculateKeyguardTranslationY);
        float f2 = getUseSplitShade() ? this.alpha : -1.0f;
        this.statusBarAlpha = f2;
        this.notificationPanelController.setKeyguardStatusBarAlpha(f2);
    }

    public final int calculateKeyguardTranslationY(float f) {
        if (!getUseSplitShade()) {
            return 0;
        }
        if (this.mediaHierarchyManager.isCurrentlyInGuidedTransformation()) {
            return this.mediaHierarchyManager.getGuidedTransformationTranslationY();
        }
        float saturate = MathUtils.saturate(f / ((float) this.keyguardTransitionDistance));
        this.translationYProgress = saturate;
        return (int) (saturate * ((float) this.keyguardTransitionOffset));
    }

    public void dump(@NotNull IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("LockscreenShadeKeyguardTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Resources:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("alphaTransitionDistance: ", Integer.valueOf(this.alphaTransitionDistance)));
        indentingPrintWriter.println(Intrinsics.stringPlus("keyguardTransitionDistance: ", Integer.valueOf(this.keyguardTransitionDistance)));
        indentingPrintWriter.println(Intrinsics.stringPlus("keyguardTransitionOffset: ", Integer.valueOf(this.keyguardTransitionOffset)));
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("State:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("dragDownAmount: ", Float.valueOf(getDragDownAmount())));
        indentingPrintWriter.println(Intrinsics.stringPlus("alpha: ", Float.valueOf(this.alpha)));
        indentingPrintWriter.println(Intrinsics.stringPlus("alphaProgress: ", Float.valueOf(this.alphaProgress)));
        indentingPrintWriter.println(Intrinsics.stringPlus("statusBarAlpha: ", Float.valueOf(this.statusBarAlpha)));
        indentingPrintWriter.println(Intrinsics.stringPlus("translationProgress: ", Float.valueOf(this.translationYProgress)));
        indentingPrintWriter.println(Intrinsics.stringPlus("translationY: ", Integer.valueOf(this.translationY)));
    }
}
