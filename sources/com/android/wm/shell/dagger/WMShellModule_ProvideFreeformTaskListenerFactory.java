package com.android.wm.shell.dagger;

import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.freeform.FreeformTaskListener;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellModule_ProvideFreeformTaskListenerFactory implements Factory<FreeformTaskListener> {
    public final Provider<SyncTransactionQueue> syncQueueProvider;

    public WMShellModule_ProvideFreeformTaskListenerFactory(Provider<SyncTransactionQueue> provider) {
        this.syncQueueProvider = provider;
    }

    public FreeformTaskListener get() {
        return provideFreeformTaskListener(this.syncQueueProvider.get());
    }

    public static WMShellModule_ProvideFreeformTaskListenerFactory create(Provider<SyncTransactionQueue> provider) {
        return new WMShellModule_ProvideFreeformTaskListenerFactory(provider);
    }

    public static FreeformTaskListener provideFreeformTaskListener(SyncTransactionQueue syncTransactionQueue) {
        return (FreeformTaskListener) Preconditions.checkNotNullFromProvides(WMShellModule.provideFreeformTaskListener(syncTransactionQueue));
    }
}
