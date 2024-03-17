package com.android.systemui.statusbar.notification;

import android.view.View;
import android.view.ViewGroup;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationLaunchAnimatorController.kt */
public final class NotificationLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final HeadsUpManagerPhone headsUpManager;
    @NotNull
    public final InteractionJankMonitor jankMonitor;
    @NotNull
    public final ExpandableNotificationRow notification;
    @NotNull
    public final NotificationEntry notificationEntry;
    public final String notificationKey;
    @NotNull
    public final NotificationListContainer notificationListContainer;
    @NotNull
    public final NotificationShadeWindowViewController notificationShadeWindowViewController;
    @Nullable
    public final Runnable onFinishAnimationCallback;

    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
    }

    public NotificationLaunchAnimatorController(@NotNull NotificationShadeWindowViewController notificationShadeWindowViewController2, @NotNull NotificationListContainer notificationListContainer2, @NotNull HeadsUpManagerPhone headsUpManagerPhone, @NotNull ExpandableNotificationRow expandableNotificationRow, @NotNull InteractionJankMonitor interactionJankMonitor, @Nullable Runnable runnable) {
        this.notificationShadeWindowViewController = notificationShadeWindowViewController2;
        this.notificationListContainer = notificationListContainer2;
        this.headsUpManager = headsUpManagerPhone;
        this.notification = expandableNotificationRow;
        this.jankMonitor = interactionJankMonitor;
        this.onFinishAnimationCallback = runnable;
        NotificationEntry entry = expandableNotificationRow.getEntry();
        this.notificationEntry = entry;
        this.notificationKey = entry.getSbn().getKey();
    }

    /* compiled from: NotificationLaunchAnimatorController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        View rootView = this.notification.getRootView();
        if (rootView != null) {
            return (ViewGroup) rootView;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        float f;
        int max = Math.max(0, this.notification.getActualHeight() - this.notification.getClipBottomAmount());
        int[] locationOnScreen = this.notification.getLocationOnScreen();
        int topClippingStartLocation = this.notificationListContainer.getTopClippingStartLocation();
        int max2 = Math.max(topClippingStartLocation - locationOnScreen[1], 0);
        int i = locationOnScreen[1] + max2;
        if (max2 > 0) {
            f = 0.0f;
        } else {
            f = this.notification.getCurrentBackgroundRadiusTop();
        }
        int i2 = locationOnScreen[0];
        LaunchAnimationParameters launchAnimationParameters = new LaunchAnimationParameters(i, locationOnScreen[1] + max, i2, i2 + this.notification.getWidth(), f, this.notification.getCurrentBackgroundRadiusBottom());
        launchAnimationParameters.setStartTranslationZ(this.notification.getTranslationZ());
        launchAnimationParameters.setStartNotificationTop(this.notification.getTranslationY());
        launchAnimationParameters.setStartRoundedTopClipping(max2);
        launchAnimationParameters.setStartClipTopAmount(this.notification.getClipTopAmount());
        if (this.notification.isChildInGroup()) {
            launchAnimationParameters.setStartNotificationTop(launchAnimationParameters.getStartNotificationTop() + this.notification.getNotificationParent().getTranslationY());
            launchAnimationParameters.setParentStartRoundedTopClipping(Math.max(topClippingStartLocation - this.notification.getNotificationParent().getLocationOnScreen()[1], 0));
            int clipTopAmount = this.notification.getNotificationParent().getClipTopAmount();
            launchAnimationParameters.setParentStartClipTopAmount(clipTopAmount);
            if (clipTopAmount != 0) {
                float translationY = ((float) clipTopAmount) - this.notification.getTranslationY();
                if (translationY > 0.0f) {
                    launchAnimationParameters.setStartClipTopAmount((int) Math.ceil((double) translationY));
                }
            }
        }
        return launchAnimationParameters;
    }

    public void onIntentStarted(boolean z) {
        this.notificationShadeWindowViewController.setExpandAnimationRunning(z);
        this.notificationEntry.setExpandAnimationRunning(z);
        if (!z) {
            removeHun(true);
            Runnable runnable = this.onFinishAnimationCallback;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public final void removeHun(boolean z) {
        if (this.headsUpManager.isAlerting(this.notificationKey)) {
            HeadsUpUtil.setNeedsHeadsUpDisappearAnimationAfterClick(this.notification, z);
            this.headsUpManager.removeNotification(this.notificationKey, true, z);
        }
    }

    public void onLaunchAnimationCancelled() {
        this.notificationShadeWindowViewController.setExpandAnimationRunning(false);
        this.notificationEntry.setExpandAnimationRunning(false);
        removeHun(true);
        Runnable runnable = this.onFinishAnimationCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void onLaunchAnimationStart(boolean z) {
        this.notification.setExpandAnimationRunning(true);
        this.notificationListContainer.setExpandingNotification(this.notification);
        this.jankMonitor.begin(this.notification, 16);
    }

    public void onLaunchAnimationEnd(boolean z) {
        this.jankMonitor.end(16);
        this.notification.setExpandAnimationRunning(false);
        this.notificationShadeWindowViewController.setExpandAnimationRunning(false);
        this.notificationEntry.setExpandAnimationRunning(false);
        this.notificationListContainer.setExpandingNotification((ExpandableNotificationRow) null);
        applyParams((LaunchAnimationParameters) null);
        removeHun(false);
        Runnable runnable = this.onFinishAnimationCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void applyParams(LaunchAnimationParameters launchAnimationParameters) {
        this.notification.applyLaunchAnimationParams(launchAnimationParameters);
        this.notificationListContainer.applyLaunchAnimationParams(launchAnimationParameters);
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        LaunchAnimationParameters launchAnimationParameters = (LaunchAnimationParameters) state;
        launchAnimationParameters.setProgress(f);
        launchAnimationParameters.setLinearProgress(f2);
        applyParams(launchAnimationParameters);
    }
}
