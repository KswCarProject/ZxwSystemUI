package com.android.systemui.util.concurrency;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MessageRouterImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MessageRouterImpl f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ MessageRouterImpl$$ExternalSyntheticLambda1(MessageRouterImpl messageRouterImpl, Object obj) {
        this.f$0 = messageRouterImpl;
        this.f$1 = obj;
    }

    public final void run() {
        this.f$0.lambda$sendMessageDelayed$1(this.f$1);
    }
}
