package com.android.systemui.controls.controller;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$settingObserver$1 extends ContentObserver {
    public final /* synthetic */ ControlsControllerImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlsControllerImpl$settingObserver$1(ControlsControllerImpl controlsControllerImpl) {
        super((Handler) null);
        this.this$0 = controlsControllerImpl;
    }

    public void onChange(boolean z, @NotNull Collection<? extends Uri> collection, int i, int i2) {
        if (!this.this$0.userChanging && i2 == this.this$0.getCurrentUserId()) {
            this.this$0.resetFavorites();
        }
    }
}
