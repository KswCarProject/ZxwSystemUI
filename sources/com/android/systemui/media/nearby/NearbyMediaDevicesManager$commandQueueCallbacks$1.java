package com.android.systemui.media.nearby;

import android.media.INearbyMediaDevicesProvider;
import android.media.INearbyMediaDevicesUpdateCallback;
import com.android.systemui.statusbar.CommandQueue;
import org.jetbrains.annotations.NotNull;

/* compiled from: NearbyMediaDevicesManager.kt */
public final class NearbyMediaDevicesManager$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    public final /* synthetic */ NearbyMediaDevicesManager this$0;

    public NearbyMediaDevicesManager$commandQueueCallbacks$1(NearbyMediaDevicesManager nearbyMediaDevicesManager) {
        this.this$0 = nearbyMediaDevicesManager;
    }

    public void registerNearbyMediaDevicesProvider(@NotNull INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        if (!this.this$0.providers.contains(iNearbyMediaDevicesProvider)) {
            for (INearbyMediaDevicesUpdateCallback registerNearbyDevicesCallback : this.this$0.activeCallbacks) {
                iNearbyMediaDevicesProvider.registerNearbyDevicesCallback(registerNearbyDevicesCallback);
            }
            this.this$0.providers.add(iNearbyMediaDevicesProvider);
            this.this$0.logger.logProviderRegistered(this.this$0.providers.size());
            iNearbyMediaDevicesProvider.asBinder().linkToDeath(this.this$0.deathRecipient, 0);
        }
    }

    public void unregisterNearbyMediaDevicesProvider(@NotNull INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        if (this.this$0.providers.remove(iNearbyMediaDevicesProvider)) {
            this.this$0.logger.logProviderUnregistered(this.this$0.providers.size());
        }
    }
}
