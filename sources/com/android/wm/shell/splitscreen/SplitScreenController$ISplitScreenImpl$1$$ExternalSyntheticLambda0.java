package com.android.wm.shell.splitscreen;

import com.android.wm.shell.common.SingleInstanceRemoteListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda0 implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda0(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void accept(Object obj) {
        ((ISplitScreenListener) obj).onStagePositionChanged(this.f$0, this.f$1);
    }
}
