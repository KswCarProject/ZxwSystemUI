package com.android.wm.shell.dagger;

import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideSyncTransactionQueueFactory implements Factory<SyncTransactionQueue> {
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<TransactionPool> poolProvider;

    public WMShellBaseModule_ProvideSyncTransactionQueueFactory(Provider<TransactionPool> provider, Provider<ShellExecutor> provider2) {
        this.poolProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    public SyncTransactionQueue get() {
        return provideSyncTransactionQueue(this.poolProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideSyncTransactionQueueFactory create(Provider<TransactionPool> provider, Provider<ShellExecutor> provider2) {
        return new WMShellBaseModule_ProvideSyncTransactionQueueFactory(provider, provider2);
    }

    public static SyncTransactionQueue provideSyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        return (SyncTransactionQueue) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSyncTransactionQueue(transactionPool, shellExecutor));
    }
}
