package com.android.systemui.dagger;

import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;

public abstract class PluginModule {
    public static ActivityStarter provideActivityStarter(ActivityStarterDelegate activityStarterDelegate, PluginDependencyProvider pluginDependencyProvider) {
        pluginDependencyProvider.allowPluginDependency(ActivityStarter.class, activityStarterDelegate);
        return activityStarterDelegate;
    }
}
