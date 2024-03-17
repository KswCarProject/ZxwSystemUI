package com.android.systemui.shared.plugins;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PluginActionManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PluginActionManager f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ PluginActionManager$$ExternalSyntheticLambda2(PluginActionManager pluginActionManager, String str) {
        this.f$0 = pluginActionManager;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.lambda$onPackageRemoved$1(this.f$1);
    }
}
