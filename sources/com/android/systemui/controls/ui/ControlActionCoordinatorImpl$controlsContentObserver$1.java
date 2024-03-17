package com.android.systemui.controls.ui;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$controlsContentObserver$1 extends ContentObserver {
    public final /* synthetic */ Uri $lockScreenShowControlsUri;
    public final /* synthetic */ Handler $mainHandler;
    public final /* synthetic */ Uri $showControlsUri;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$controlsContentObserver$1(Handler handler, Uri uri, ControlActionCoordinatorImpl controlActionCoordinatorImpl, Uri uri2) {
        super(handler);
        this.$mainHandler = handler;
        this.$lockScreenShowControlsUri = uri;
        this.this$0 = controlActionCoordinatorImpl;
        this.$showControlsUri = uri2;
    }

    public void onChange(boolean z, @Nullable Uri uri) {
        super.onChange(z, uri);
        boolean z2 = true;
        if (Intrinsics.areEqual((Object) uri, (Object) this.$lockScreenShowControlsUri)) {
            ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
            if (controlActionCoordinatorImpl.secureSettings.getIntForUser("lockscreen_allow_trivial_controls", 0, -2) == 0) {
                z2 = false;
            }
            controlActionCoordinatorImpl.mAllowTrivialControls = z2;
        } else if (Intrinsics.areEqual((Object) uri, (Object) this.$showControlsUri)) {
            ControlActionCoordinatorImpl controlActionCoordinatorImpl2 = this.this$0;
            if (controlActionCoordinatorImpl2.secureSettings.getIntForUser("lockscreen_show_controls", 0, -2) == 0) {
                z2 = false;
            }
            controlActionCoordinatorImpl2.mShowDeviceControlsInLockscreen = z2;
        }
    }
}
