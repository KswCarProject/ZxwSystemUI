package com.android.systemui.media.nearby;

import android.media.INearbyMediaDevicesProvider;
import android.media.INearbyMediaDevicesUpdateCallback;
import android.os.IBinder;
import com.android.systemui.statusbar.CommandQueue;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: NearbyMediaDevicesManager.kt */
public final class NearbyMediaDevicesManager {
    @NotNull
    public List<INearbyMediaDevicesUpdateCallback> activeCallbacks = new ArrayList();
    @NotNull
    public final NearbyMediaDevicesManager$commandQueueCallbacks$1 commandQueueCallbacks;
    @NotNull
    public final NearbyMediaDevicesManager$deathRecipient$1 deathRecipient;
    @NotNull
    public final NearbyMediaDevicesLogger logger;
    @NotNull
    public List<INearbyMediaDevicesProvider> providers = new ArrayList();

    public NearbyMediaDevicesManager(@NotNull CommandQueue commandQueue, @NotNull NearbyMediaDevicesLogger nearbyMediaDevicesLogger) {
        this.logger = nearbyMediaDevicesLogger;
        NearbyMediaDevicesManager$commandQueueCallbacks$1 nearbyMediaDevicesManager$commandQueueCallbacks$1 = new NearbyMediaDevicesManager$commandQueueCallbacks$1(this);
        this.commandQueueCallbacks = nearbyMediaDevicesManager$commandQueueCallbacks$1;
        this.deathRecipient = new NearbyMediaDevicesManager$deathRecipient$1(this);
        commandQueue.addCallback((CommandQueue.Callbacks) nearbyMediaDevicesManager$commandQueueCallbacks$1);
    }

    public final void registerNearbyDevicesCallback(@NotNull INearbyMediaDevicesUpdateCallback iNearbyMediaDevicesUpdateCallback) {
        for (INearbyMediaDevicesProvider registerNearbyDevicesCallback : this.providers) {
            registerNearbyDevicesCallback.registerNearbyDevicesCallback(iNearbyMediaDevicesUpdateCallback);
        }
        this.activeCallbacks.add(iNearbyMediaDevicesUpdateCallback);
    }

    public final void unregisterNearbyDevicesCallback(@NotNull INearbyMediaDevicesUpdateCallback iNearbyMediaDevicesUpdateCallback) {
        this.activeCallbacks.remove(iNearbyMediaDevicesUpdateCallback);
        for (INearbyMediaDevicesProvider unregisterNearbyDevicesCallback : this.providers) {
            unregisterNearbyDevicesCallback.unregisterNearbyDevicesCallback(iNearbyMediaDevicesUpdateCallback);
        }
    }

    public final void binderDiedInternal(IBinder iBinder) {
        synchronized (this.providers) {
            int size = this.providers.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i = size - 1;
                    if (Intrinsics.areEqual((Object) this.providers.get(size).asBinder(), (Object) iBinder)) {
                        this.providers.remove(size);
                        this.logger.logProviderBinderDied(this.providers.size());
                        break;
                    } else if (i < 0) {
                        break;
                    } else {
                        size = i;
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
        }
    }
}
