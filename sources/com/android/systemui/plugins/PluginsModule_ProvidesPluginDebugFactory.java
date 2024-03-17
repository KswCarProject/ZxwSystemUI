package com.android.systemui.plugins;

import dagger.internal.Factory;

public final class PluginsModule_ProvidesPluginDebugFactory implements Factory<Boolean> {
    public Boolean get() {
        return Boolean.valueOf(providesPluginDebug());
    }

    public static PluginsModule_ProvidesPluginDebugFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean providesPluginDebug() {
        return PluginsModule.providesPluginDebug();
    }

    public static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final PluginsModule_ProvidesPluginDebugFactory INSTANCE = new PluginsModule_ProvidesPluginDebugFactory();

        private InstanceHolder() {
        }
    }
}
