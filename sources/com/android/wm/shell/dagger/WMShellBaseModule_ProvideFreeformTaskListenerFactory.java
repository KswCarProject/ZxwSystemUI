package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.freeform.FreeformTaskListener;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideFreeformTaskListenerFactory implements Factory<Optional<FreeformTaskListener>> {
    public final Provider<Context> contextProvider;
    public final Provider<Optional<FreeformTaskListener>> freeformTaskListenerProvider;

    public WMShellBaseModule_ProvideFreeformTaskListenerFactory(Provider<Optional<FreeformTaskListener>> provider, Provider<Context> provider2) {
        this.freeformTaskListenerProvider = provider;
        this.contextProvider = provider2;
    }

    public Optional<FreeformTaskListener> get() {
        return provideFreeformTaskListener(this.freeformTaskListenerProvider.get(), this.contextProvider.get());
    }

    public static WMShellBaseModule_ProvideFreeformTaskListenerFactory create(Provider<Optional<FreeformTaskListener>> provider, Provider<Context> provider2) {
        return new WMShellBaseModule_ProvideFreeformTaskListenerFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional<com.android.wm.shell.freeform.FreeformTaskListener>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.freeform.FreeformTaskListener> provideFreeformTaskListener(java.util.Optional<com.android.wm.shell.freeform.FreeformTaskListener> r0, android.content.Context r1) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellBaseModule.provideFreeformTaskListener(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellBaseModule_ProvideFreeformTaskListenerFactory.provideFreeformTaskListener(java.util.Optional, android.content.Context):java.util.Optional");
    }
}
