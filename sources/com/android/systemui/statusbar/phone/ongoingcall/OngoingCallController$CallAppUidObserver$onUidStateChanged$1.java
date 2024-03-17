package com.android.systemui.statusbar.phone.ongoingcall;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$CallAppUidObserver$onUidStateChanged$1 implements Runnable {
    public final /* synthetic */ OngoingCallController this$0;

    public OngoingCallController$CallAppUidObserver$onUidStateChanged$1(OngoingCallController ongoingCallController) {
        this.this$0 = ongoingCallController;
    }

    public final void run() {
        for (OngoingCallListener onOngoingCallStateChanged : this.this$0.mListeners) {
            onOngoingCallStateChanged.onOngoingCallStateChanged(true);
        }
    }
}
