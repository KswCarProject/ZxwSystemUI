package com.android.systemui.media.nearby;

import android.os.IBinder;
import org.jetbrains.annotations.NotNull;

/* compiled from: NearbyMediaDevicesManager.kt */
public final class NearbyMediaDevicesManager$deathRecipient$1 implements IBinder.DeathRecipient {
    public final /* synthetic */ NearbyMediaDevicesManager this$0;

    public void binderDied() {
    }

    public NearbyMediaDevicesManager$deathRecipient$1(NearbyMediaDevicesManager nearbyMediaDevicesManager) {
        this.this$0 = nearbyMediaDevicesManager;
    }

    public void binderDied(@NotNull IBinder iBinder) {
        this.this$0.binderDiedInternal(iBinder);
    }
}
