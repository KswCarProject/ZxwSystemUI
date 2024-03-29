package com.android.systemui.globalactions;

import android.content.Context;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ExtensionController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GlobalActionsComponent_Factory implements Factory<GlobalActionsComponent> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ExtensionController> extensionControllerProvider;
    public final Provider<GlobalActions> globalActionsProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;

    public GlobalActionsComponent_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ExtensionController> provider3, Provider<GlobalActions> provider4, Provider<StatusBarKeyguardViewManager> provider5) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.extensionControllerProvider = provider3;
        this.globalActionsProvider = provider4;
        this.statusBarKeyguardViewManagerProvider = provider5;
    }

    public GlobalActionsComponent get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.extensionControllerProvider.get(), this.globalActionsProvider, this.statusBarKeyguardViewManagerProvider.get());
    }

    public static GlobalActionsComponent_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ExtensionController> provider3, Provider<GlobalActions> provider4, Provider<StatusBarKeyguardViewManager> provider5) {
        return new GlobalActionsComponent_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static GlobalActionsComponent newInstance(Context context, CommandQueue commandQueue, ExtensionController extensionController, Provider<GlobalActions> provider, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        return new GlobalActionsComponent(context, commandQueue, extensionController, provider, statusBarKeyguardViewManager);
    }
}
