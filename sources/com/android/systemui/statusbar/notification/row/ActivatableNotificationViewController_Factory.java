package com.android.systemui.statusbar.notification.row;

import android.view.accessibility.AccessibilityManager;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ActivatableNotificationViewController_Factory implements Factory<ActivatableNotificationViewController> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<NotificationTapHelper.Factory> notificationTapHelpFactoryProvider;
    public final Provider<ActivatableNotificationView> viewProvider;

    public ActivatableNotificationViewController_Factory(Provider<ActivatableNotificationView> provider, Provider<NotificationTapHelper.Factory> provider2, Provider<ExpandableOutlineViewController> provider3, Provider<AccessibilityManager> provider4, Provider<FalsingManager> provider5, Provider<FalsingCollector> provider6) {
        this.viewProvider = provider;
        this.notificationTapHelpFactoryProvider = provider2;
        this.expandableOutlineViewControllerProvider = provider3;
        this.accessibilityManagerProvider = provider4;
        this.falsingManagerProvider = provider5;
        this.falsingCollectorProvider = provider6;
    }

    public ActivatableNotificationViewController get() {
        return newInstance(this.viewProvider.get(), this.notificationTapHelpFactoryProvider.get(), this.expandableOutlineViewControllerProvider.get(), this.accessibilityManagerProvider.get(), this.falsingManagerProvider.get(), this.falsingCollectorProvider.get());
    }

    public static ActivatableNotificationViewController_Factory create(Provider<ActivatableNotificationView> provider, Provider<NotificationTapHelper.Factory> provider2, Provider<ExpandableOutlineViewController> provider3, Provider<AccessibilityManager> provider4, Provider<FalsingManager> provider5, Provider<FalsingCollector> provider6) {
        return new ActivatableNotificationViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static ActivatableNotificationViewController newInstance(ActivatableNotificationView activatableNotificationView, NotificationTapHelper.Factory factory, ExpandableOutlineViewController expandableOutlineViewController, AccessibilityManager accessibilityManager, FalsingManager falsingManager, FalsingCollector falsingCollector) {
        return new ActivatableNotificationViewController(activatableNotificationView, factory, expandableOutlineViewController, accessibilityManager, falsingManager, falsingCollector);
    }
}
