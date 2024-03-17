package com.android.wm.shell.startingsurface;

import com.android.wm.shell.common.SingleInstanceRemoteListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda4 implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ Integer f$0;
    public final /* synthetic */ Integer f$1;
    public final /* synthetic */ Integer f$2;

    public /* synthetic */ StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda4(Integer num, Integer num2, Integer num3) {
        this.f$0 = num;
        this.f$1 = num2;
        this.f$2 = num3;
    }

    public final void accept(Object obj) {
        ((IStartingWindowListener) obj).onTaskLaunching(this.f$0.intValue(), this.f$1.intValue(), this.f$2.intValue());
    }
}
