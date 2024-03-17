package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController_Factory  reason: case insensitive filesystem */
public final class C0001LockscreenShadeKeyguardTransitionController_Factory {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;

    public C0001LockscreenShadeKeyguardTransitionController_Factory(Provider<MediaHierarchyManager> provider, Provider<Context> provider2, Provider<ConfigurationController> provider3, Provider<DumpManager> provider4) {
        this.mediaHierarchyManagerProvider = provider;
        this.contextProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public LockscreenShadeKeyguardTransitionController get(NotificationPanelViewController notificationPanelViewController) {
        return newInstance(this.mediaHierarchyManagerProvider.get(), notificationPanelViewController, this.contextProvider.get(), this.configurationControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static C0001LockscreenShadeKeyguardTransitionController_Factory create(Provider<MediaHierarchyManager> provider, Provider<Context> provider2, Provider<ConfigurationController> provider3, Provider<DumpManager> provider4) {
        return new C0001LockscreenShadeKeyguardTransitionController_Factory(provider, provider2, provider3, provider4);
    }

    public static LockscreenShadeKeyguardTransitionController newInstance(MediaHierarchyManager mediaHierarchyManager, NotificationPanelViewController notificationPanelViewController, Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        return new LockscreenShadeKeyguardTransitionController(mediaHierarchyManager, notificationPanelViewController, context, configurationController, dumpManager);
    }
}
