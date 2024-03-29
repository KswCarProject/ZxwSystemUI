package com.android.systemui.statusbar.notification.icon;

import android.content.pm.LauncherApps;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class IconManager_Factory implements Factory<IconManager> {
    public final Provider<IconBuilder> iconBuilderProvider;
    public final Provider<LauncherApps> launcherAppsProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;

    public IconManager_Factory(Provider<CommonNotifCollection> provider, Provider<LauncherApps> provider2, Provider<IconBuilder> provider3) {
        this.notifCollectionProvider = provider;
        this.launcherAppsProvider = provider2;
        this.iconBuilderProvider = provider3;
    }

    public IconManager get() {
        return newInstance(this.notifCollectionProvider.get(), this.launcherAppsProvider.get(), this.iconBuilderProvider.get());
    }

    public static IconManager_Factory create(Provider<CommonNotifCollection> provider, Provider<LauncherApps> provider2, Provider<IconBuilder> provider3) {
        return new IconManager_Factory(provider, provider2, provider3);
    }

    public static IconManager newInstance(CommonNotifCollection commonNotifCollection, LauncherApps launcherApps, IconBuilder iconBuilder) {
        return new IconManager(commonNotifCollection, launcherApps, iconBuilder);
    }
}
