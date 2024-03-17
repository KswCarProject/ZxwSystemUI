package com.android.wm.shell.splitscreen;

import com.android.wm.shell.common.SingleInstanceRemoteListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda1 implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda1(int i, int i2, boolean z) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = z;
    }

    public final void accept(Object obj) {
        ((ISplitScreenListener) obj).onTaskStageChanged(this.f$0, this.f$1, this.f$2);
    }
}
