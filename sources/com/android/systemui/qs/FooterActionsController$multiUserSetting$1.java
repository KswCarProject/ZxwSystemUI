package com.android.systemui.qs;

import android.os.Handler;
import com.android.systemui.util.settings.GlobalSettings;

/* compiled from: FooterActionsController.kt */
public final class FooterActionsController$multiUserSetting$1 extends SettingObserver {
    public final /* synthetic */ FooterActionsController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FooterActionsController$multiUserSetting$1(FooterActionsController footerActionsController, GlobalSettings globalSettings, Handler handler, int i) {
        super(globalSettings, handler, "user_switcher_enabled", i);
        this.this$0 = footerActionsController;
    }

    public void handleValueChanged(int i, boolean z) {
        if (z) {
            this.this$0.updateView();
        }
    }
}
