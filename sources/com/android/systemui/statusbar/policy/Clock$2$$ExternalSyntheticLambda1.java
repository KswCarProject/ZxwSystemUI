package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.Clock;
import java.util.Locale;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Clock$2$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Clock.AnonymousClass2 f$0;
    public final /* synthetic */ Locale f$1;

    public /* synthetic */ Clock$2$$ExternalSyntheticLambda1(Clock.AnonymousClass2 r1, Locale locale) {
        this.f$0 = r1;
        this.f$1 = locale;
    }

    public final void run() {
        this.f$0.lambda$onReceive$1(this.f$1);
    }
}