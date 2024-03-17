package com.android.systemui.qs.external;

import android.content.Intent;
import android.os.UserHandle;
import com.android.systemui.qs.external.TileLifecycleManager;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class TileLifecycleManager_Factory_Impl implements TileLifecycleManager.Factory {
    public final C0000TileLifecycleManager_Factory delegateFactory;

    public TileLifecycleManager_Factory_Impl(C0000TileLifecycleManager_Factory tileLifecycleManager_Factory) {
        this.delegateFactory = tileLifecycleManager_Factory;
    }

    public TileLifecycleManager create(Intent intent, UserHandle userHandle) {
        return this.delegateFactory.get(intent, userHandle);
    }

    public static Provider<TileLifecycleManager.Factory> create(C0000TileLifecycleManager_Factory tileLifecycleManager_Factory) {
        return InstanceFactory.create(new TileLifecycleManager_Factory_Impl(tileLifecycleManager_Factory));
    }
}
