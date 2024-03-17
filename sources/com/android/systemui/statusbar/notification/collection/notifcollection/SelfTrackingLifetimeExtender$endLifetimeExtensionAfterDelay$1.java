package com.android.systemui.statusbar.notification.collection.notifcollection;

/* compiled from: SelfTrackingLifetimeExtender.kt */
public final class SelfTrackingLifetimeExtender$endLifetimeExtensionAfterDelay$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ SelfTrackingLifetimeExtender this$0;

    public SelfTrackingLifetimeExtender$endLifetimeExtensionAfterDelay$1(SelfTrackingLifetimeExtender selfTrackingLifetimeExtender, String str) {
        this.this$0 = selfTrackingLifetimeExtender;
        this.$key = str;
    }

    public final void run() {
        this.this$0.endLifetimeExtension(this.$key);
    }
}
