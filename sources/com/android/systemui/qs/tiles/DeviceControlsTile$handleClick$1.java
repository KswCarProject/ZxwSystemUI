package com.android.systemui.qs.tiles;

import android.content.Intent;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: DeviceControlsTile.kt */
public final class DeviceControlsTile$handleClick$1 implements Runnable {
    public final /* synthetic */ ActivityLaunchAnimator.Controller $animationController;
    public final /* synthetic */ Intent $intent;
    public final /* synthetic */ DeviceControlsTile this$0;

    public DeviceControlsTile$handleClick$1(DeviceControlsTile deviceControlsTile, Intent intent, ActivityLaunchAnimator.Controller controller) {
        this.this$0 = deviceControlsTile;
        this.$intent = intent;
        this.$animationController = controller;
    }

    public final void run() {
        this.this$0.mActivityStarter.startActivity(this.$intent, true, this.$animationController, this.this$0.getState().state == 2);
    }
}
