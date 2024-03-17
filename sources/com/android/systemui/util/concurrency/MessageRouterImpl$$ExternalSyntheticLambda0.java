package com.android.systemui.util.concurrency;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MessageRouterImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MessageRouterImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MessageRouterImpl$$ExternalSyntheticLambda0(MessageRouterImpl messageRouterImpl, int i) {
        this.f$0 = messageRouterImpl;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$sendMessageDelayed$0(this.f$1);
    }
}
