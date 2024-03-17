package com.android.systemui.statusbar.phone;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationPresenter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ StatusBarNotificationPresenter f$0;

    public /* synthetic */ StatusBarNotificationPresenter$$ExternalSyntheticLambda2(StatusBarNotificationPresenter statusBarNotificationPresenter) {
        this.f$0 = statusBarNotificationPresenter;
    }

    public final void run() {
        this.f$0.maybeClosePanelForShadeEmptied();
    }
}
