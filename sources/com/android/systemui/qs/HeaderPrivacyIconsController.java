package com.android.systemui.qs;

import android.content.IntentFilter;
import android.os.UserHandle;
import android.permission.PermissionGroupUsage;
import android.permission.PermissionManager;
import android.safetycenter.SafetyCenterManager;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyChipEvent;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import java.util.List;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController {
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final AppOpsController appOpsController;
    @NotNull
    public final View.OnAttachStateChangeListener attachStateChangeListener;
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    public final String cameraSlot;
    @Nullable
    public ChipVisibilityListener chipVisibilityListener;
    @NotNull
    public final StatusIconContainer iconContainer;
    public boolean listening;
    public boolean locationIndicatorsEnabled;
    public final String locationSlot;
    public boolean micCameraIndicatorsEnabled;
    public final String micSlot;
    @NotNull
    public final PermissionManager permissionManager;
    @NotNull
    public final PrivacyItemController.Callback picCallback;
    @NotNull
    public final OngoingPrivacyChip privacyChip;
    public boolean privacyChipLogged;
    @NotNull
    public final PrivacyDialogController privacyDialogController;
    @NotNull
    public final PrivacyItemController privacyItemController;
    @NotNull
    public final PrivacyLogger privacyLogger;
    public boolean safetyCenterEnabled;
    @NotNull
    public final SafetyCenterManager safetyCenterManager;
    @NotNull
    public final HeaderPrivacyIconsController$safetyCenterReceiver$1 safetyCenterReceiver;
    @NotNull
    public final UiEventLogger uiEventLogger;
    @NotNull
    public final Executor uiExecutor;

    public HeaderPrivacyIconsController(@NotNull PrivacyItemController privacyItemController2, @NotNull UiEventLogger uiEventLogger2, @NotNull OngoingPrivacyChip ongoingPrivacyChip, @NotNull PrivacyDialogController privacyDialogController2, @NotNull PrivacyLogger privacyLogger2, @NotNull StatusIconContainer statusIconContainer, @NotNull PermissionManager permissionManager2, @NotNull Executor executor, @NotNull Executor executor2, @NotNull ActivityStarter activityStarter2, @NotNull AppOpsController appOpsController2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull SafetyCenterManager safetyCenterManager2) {
        Executor executor3 = executor;
        this.privacyItemController = privacyItemController2;
        this.uiEventLogger = uiEventLogger2;
        this.privacyChip = ongoingPrivacyChip;
        this.privacyDialogController = privacyDialogController2;
        this.privacyLogger = privacyLogger2;
        this.iconContainer = statusIconContainer;
        this.permissionManager = permissionManager2;
        this.backgroundExecutor = executor3;
        this.uiExecutor = executor2;
        this.activityStarter = activityStarter2;
        this.appOpsController = appOpsController2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.safetyCenterManager = safetyCenterManager2;
        this.cameraSlot = ongoingPrivacyChip.getResources().getString(17041560);
        this.micSlot = ongoingPrivacyChip.getResources().getString(17041572);
        this.locationSlot = ongoingPrivacyChip.getResources().getString(17041570);
        HeaderPrivacyIconsController$safetyCenterReceiver$1 headerPrivacyIconsController$safetyCenterReceiver$1 = new HeaderPrivacyIconsController$safetyCenterReceiver$1(this);
        this.safetyCenterReceiver = headerPrivacyIconsController$safetyCenterReceiver$1;
        HeaderPrivacyIconsController$attachStateChangeListener$1 headerPrivacyIconsController$attachStateChangeListener$1 = new HeaderPrivacyIconsController$attachStateChangeListener$1(this);
        this.attachStateChangeListener = headerPrivacyIconsController$attachStateChangeListener$1;
        executor3.execute(new Runnable(this) {
            public final /* synthetic */ HeaderPrivacyIconsController this$0;

            {
                this.this$0 = r1;
            }

            public final void run() {
                HeaderPrivacyIconsController headerPrivacyIconsController = this.this$0;
                headerPrivacyIconsController.safetyCenterEnabled = headerPrivacyIconsController.safetyCenterManager.isSafetyCenterEnabled();
            }
        });
        if (ongoingPrivacyChip.isAttachedToWindow()) {
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, headerPrivacyIconsController$safetyCenterReceiver$1, new IntentFilter("android.safetycenter.action.SAFETY_CENTER_ENABLED_CHANGED"), executor, (UserHandle) null, 0, (String) null, 56, (Object) null);
        }
        ongoingPrivacyChip.addOnAttachStateChangeListener(headerPrivacyIconsController$attachStateChangeListener$1);
        this.picCallback = new HeaderPrivacyIconsController$picCallback$1(this);
    }

    public final void setChipVisibilityListener(@Nullable ChipVisibilityListener chipVisibilityListener2) {
        this.chipVisibilityListener = chipVisibilityListener2;
    }

    public final boolean getChipEnabled() {
        return this.micCameraIndicatorsEnabled || this.locationIndicatorsEnabled;
    }

    public final void onParentVisible() {
        this.privacyChip.setOnClickListener(new HeaderPrivacyIconsController$onParentVisible$1(this));
        setChipVisibility(this.privacyChip.getVisibility() == 0);
        this.micCameraIndicatorsEnabled = this.privacyItemController.getMicCameraAvailable();
        this.locationIndicatorsEnabled = this.privacyItemController.getLocationAvailable();
        updatePrivacyIconSlots();
    }

    public final void showSafetyCenter() {
        this.backgroundExecutor.execute(new HeaderPrivacyIconsController$showSafetyCenter$1(this));
    }

    public final List<PermissionGroupUsage> permGroupUsage() {
        return this.permissionManager.getIndicatorAppOpUsageData(this.appOpsController.isMicMuted());
    }

    public final void onParentInvisible() {
        this.chipVisibilityListener = null;
        this.privacyChip.setOnClickListener((View.OnClickListener) null);
    }

    public final void startListening() {
        this.listening = true;
        this.micCameraIndicatorsEnabled = this.privacyItemController.getMicCameraAvailable();
        this.locationIndicatorsEnabled = this.privacyItemController.getLocationAvailable();
        this.privacyItemController.addCallback(this.picCallback);
    }

    public final void stopListening() {
        this.listening = false;
        this.privacyItemController.removeCallback(this.picCallback);
        this.privacyChipLogged = false;
    }

    public final void setChipVisibility(boolean z) {
        int i = 0;
        if (!z || !getChipEnabled()) {
            this.privacyLogger.logChipVisible(false);
        } else {
            this.privacyLogger.logChipVisible(true);
            if (!this.privacyChipLogged && this.listening) {
                this.privacyChipLogged = true;
                this.uiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_VIEW);
            }
        }
        OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
        if (!z) {
            i = 8;
        }
        ongoingPrivacyChip.setVisibility(i);
        ChipVisibilityListener chipVisibilityListener2 = this.chipVisibilityListener;
        if (chipVisibilityListener2 != null) {
            chipVisibilityListener2.onChipVisibilityRefreshed(z);
        }
    }

    public final void updatePrivacyIconSlots() {
        if (getChipEnabled()) {
            if (this.micCameraIndicatorsEnabled) {
                this.iconContainer.addIgnoredSlot(this.cameraSlot);
                this.iconContainer.addIgnoredSlot(this.micSlot);
            } else {
                this.iconContainer.removeIgnoredSlot(this.cameraSlot);
                this.iconContainer.removeIgnoredSlot(this.micSlot);
            }
            if (this.locationIndicatorsEnabled) {
                this.iconContainer.addIgnoredSlot(this.locationSlot);
            } else {
                this.iconContainer.removeIgnoredSlot(this.locationSlot);
            }
        } else {
            this.iconContainer.removeIgnoredSlot(this.cameraSlot);
            this.iconContainer.removeIgnoredSlot(this.micSlot);
            this.iconContainer.removeIgnoredSlot(this.locationSlot);
        }
    }
}
