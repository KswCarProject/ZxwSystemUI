package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import org.jetbrains.annotations.NotNull;

/* compiled from: UdfpsFpmOtherViewController.kt */
public final class UdfpsFpmOtherViewController extends UdfpsAnimationViewController<UdfpsFpmOtherView> {
    @NotNull
    public final String tag = "UdfpsFpmOtherViewController";

    public UdfpsFpmOtherViewController(@NotNull UdfpsFpmOtherView udfpsFpmOtherView, @NotNull StatusBarStateController statusBarStateController, @NotNull PanelExpansionStateManager panelExpansionStateManager, @NotNull SystemUIDialogManager systemUIDialogManager, @NotNull DumpManager dumpManager) {
        super(udfpsFpmOtherView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
    }

    @NotNull
    public String getTag() {
        return this.tag;
    }
}
