package com.android.systemui.statusbar.notification.collection;

import android.view.Choreographer;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifPipelineChoreographerImpl_Factory implements Factory<NotifPipelineChoreographerImpl> {
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<Choreographer> viewChoreographerProvider;

    public NotifPipelineChoreographerImpl_Factory(Provider<Choreographer> provider, Provider<DelayableExecutor> provider2) {
        this.viewChoreographerProvider = provider;
        this.executorProvider = provider2;
    }

    public NotifPipelineChoreographerImpl get() {
        return newInstance(this.viewChoreographerProvider.get(), this.executorProvider.get());
    }

    public static NotifPipelineChoreographerImpl_Factory create(Provider<Choreographer> provider, Provider<DelayableExecutor> provider2) {
        return new NotifPipelineChoreographerImpl_Factory(provider, provider2);
    }

    public static NotifPipelineChoreographerImpl newInstance(Choreographer choreographer, DelayableExecutor delayableExecutor) {
        return new NotifPipelineChoreographerImpl(choreographer, delayableExecutor);
    }
}
