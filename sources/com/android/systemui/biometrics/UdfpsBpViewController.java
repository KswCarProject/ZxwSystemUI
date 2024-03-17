package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import org.jetbrains.annotations.NotNull;

/* compiled from: UdfpsBpViewController.kt */
public final class UdfpsBpViewController extends UdfpsAnimationViewController<UdfpsBpView> {
    @NotNull
    public final String tag = "UdfpsBpViewController";

    public UdfpsBpViewController(@NotNull UdfpsBpView udfpsBpView, @NotNull StatusBarStateController statusBarStateController, @NotNull PanelExpansionStateManager panelExpansionStateManager, @NotNull SystemUIDialogManager systemUIDialogManager, @NotNull DumpManager dumpManager) {
        super(udfpsBpView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
    }

    @NotNull
    public String getTag() {
        return this.tag;
    }
}
