package com.android.wm.shell.common;

import com.android.wm.shell.common.SingleInstanceRemoteListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SingleInstanceRemoteListener$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SingleInstanceRemoteListener.AnonymousClass1 f$0;
    public final /* synthetic */ RemoteCallable f$1;

    public /* synthetic */ SingleInstanceRemoteListener$1$$ExternalSyntheticLambda0(SingleInstanceRemoteListener.AnonymousClass1 r1, RemoteCallable remoteCallable) {
        this.f$0 = r1;
        this.f$1 = remoteCallable;
    }

    public final void run() {
        this.f$0.lambda$binderDied$0(this.f$1);
    }
}
