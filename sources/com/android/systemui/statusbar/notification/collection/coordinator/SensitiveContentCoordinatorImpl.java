package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.List;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: SensitiveContentCoordinator.kt */
public final class SensitiveContentCoordinatorImpl extends Invalidator implements SensitiveContentCoordinator, DynamicPrivacyController.Listener, OnBeforeRenderListListener {
    @NotNull
    public final DynamicPrivacyController dynamicPrivacyController;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public final NotificationLockscreenUserManager lockscreenUserManager;
    @NotNull
    public final StatusBarStateController statusBarStateController;

    public SensitiveContentCoordinatorImpl(@NotNull DynamicPrivacyController dynamicPrivacyController2, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2, @NotNull StatusBarStateController statusBarStateController2, @NotNull KeyguardStateController keyguardStateController2) {
        super("SensitiveContentInvalidator");
        this.dynamicPrivacyController = dynamicPrivacyController2;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.statusBarStateController = statusBarStateController2;
        this.keyguardStateController = keyguardStateController2;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        this.dynamicPrivacyController.addListener(this);
        notifPipeline.addOnBeforeRenderListListener(this);
        notifPipeline.addPreRenderInvalidator(this);
    }

    public void onDynamicPrivacyChanged() {
        invalidateList();
    }

    public void onBeforeRenderList(@NotNull List<? extends ListEntry> list) {
        boolean z;
        if (this.keyguardStateController.isKeyguardGoingAway()) {
            return;
        }
        if (this.statusBarStateController.getState() != 1 || !this.keyguardUpdateMonitor.getUserUnlockedWithBiometricAndIsBypassing(KeyguardUpdateMonitor.getCurrentUser())) {
            int currentUserId = this.lockscreenUserManager.getCurrentUserId();
            boolean isLockscreenPublicMode = this.lockscreenUserManager.isLockscreenPublicMode(currentUserId);
            boolean z2 = isLockscreenPublicMode && !this.lockscreenUserManager.userAllowsPrivateNotificationsInPublic(currentUserId);
            boolean isDynamicallyUnlocked = this.dynamicPrivacyController.isDynamicallyUnlocked();
            for (NotificationEntry notificationEntry : SequencesKt___SequencesKt.filter(SensitiveContentCoordinatorKt.extractAllRepresentativeEntries(list), SensitiveContentCoordinatorImpl$onBeforeRenderList$1.INSTANCE)) {
                int identifier = notificationEntry.getSbn().getUser().getIdentifier();
                if (isLockscreenPublicMode || this.lockscreenUserManager.isLockscreenPublicMode(identifier)) {
                    if (!isDynamicallyUnlocked) {
                        z = true;
                    } else if (!(identifier == currentUserId || identifier == -1)) {
                        z = this.lockscreenUserManager.needsSeparateWorkChallenge(identifier);
                    }
                    notificationEntry.setSensitive(!z && this.lockscreenUserManager.needsRedaction(notificationEntry), z2);
                }
                z = false;
                notificationEntry.setSensitive(!z && this.lockscreenUserManager.needsRedaction(notificationEntry), z2);
            }
        }
    }
}
