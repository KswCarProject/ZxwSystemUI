package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Configuration;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteInputQuickSettingsDisabler.kt */
public final class RemoteInputQuickSettingsDisabler implements ConfigurationController.ConfigurationListener {
    @NotNull
    public final CommandQueue commandQueue;
    @NotNull
    public final Context context;
    public boolean isLandscape;
    public boolean remoteInputActive;
    public boolean shouldUseSplitNotificationShade;

    public RemoteInputQuickSettingsDisabler(@NotNull Context context2, @NotNull CommandQueue commandQueue2, @NotNull ConfigurationController configurationController) {
        this.context = context2;
        this.commandQueue = commandQueue2;
        this.isLandscape = context2.getResources().getConfiguration().orientation == 2;
        this.shouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(context2.getResources());
        configurationController.addCallback(this);
    }

    public final int adjustDisableFlags(int i) {
        return (!this.remoteInputActive || !this.isLandscape || this.shouldUseSplitNotificationShade) ? i : i | 1;
    }

    public final void setRemoteInputActive(boolean z) {
        if (this.remoteInputActive != z) {
            this.remoteInputActive = z;
            recomputeDisableFlags();
        }
    }

    public void onConfigChanged(@NotNull Configuration configuration) {
        boolean z = false;
        boolean z2 = true;
        boolean z3 = configuration.orientation == 2;
        if (z3 != this.isLandscape) {
            this.isLandscape = z3;
            z = true;
        }
        boolean shouldUseSplitNotificationShade2 = LargeScreenUtils.shouldUseSplitNotificationShade(this.context.getResources());
        if (shouldUseSplitNotificationShade2 != this.shouldUseSplitNotificationShade) {
            this.shouldUseSplitNotificationShade = shouldUseSplitNotificationShade2;
        } else {
            z2 = z;
        }
        if (z2) {
            recomputeDisableFlags();
        }
    }

    public final void recomputeDisableFlags() {
        this.commandQueue.recomputeDisableFlags(this.context.getDisplayId(), true);
    }
}
