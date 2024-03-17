package com.android.systemui.statusbar.phone;

import android.app.TaskStackBuilder;
import android.view.RemoteAnimationAdapter;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import kotlin.jvm.functions.Function1;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1 implements Function1 {
    public final /* synthetic */ StatusBarNotificationActivityStarter.AnonymousClass5 f$0;
    public final /* synthetic */ TaskStackBuilder f$1;

    public /* synthetic */ StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1(StatusBarNotificationActivityStarter.AnonymousClass5 r1, TaskStackBuilder taskStackBuilder) {
        this.f$0 = r1;
        this.f$1 = taskStackBuilder;
    }

    public final Object invoke(Object obj) {
        return this.f$0.lambda$onDismiss$0(this.f$1, (RemoteAnimationAdapter) obj);
    }
}
