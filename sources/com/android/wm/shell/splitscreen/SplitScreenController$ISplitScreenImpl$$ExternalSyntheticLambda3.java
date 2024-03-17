package com.android.wm.shell.splitscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.RemoteAnimationAdapter;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ Bundle f$4;
    public final /* synthetic */ int f$5;
    public final /* synthetic */ float f$6;
    public final /* synthetic */ RemoteAnimationAdapter f$7;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
        this.f$0 = pendingIntent;
        this.f$1 = intent;
        this.f$2 = i;
        this.f$3 = bundle;
        this.f$4 = bundle2;
        this.f$5 = i2;
        this.f$6 = f;
        this.f$7 = remoteAnimationAdapter;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).mStageCoordinator.startIntentAndTaskWithLegacyTransition(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
