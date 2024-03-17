package com.android.systemui.statusbar;

import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class LockscreenShadeKeyguardTransitionController_Factory_Impl implements LockscreenShadeKeyguardTransitionController.Factory {
    public final C0001LockscreenShadeKeyguardTransitionController_Factory delegateFactory;

    public LockscreenShadeKeyguardTransitionController_Factory_Impl(C0001LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionController_Factory) {
        this.delegateFactory = lockscreenShadeKeyguardTransitionController_Factory;
    }

    public LockscreenShadeKeyguardTransitionController create(NotificationPanelViewController notificationPanelViewController) {
        return this.delegateFactory.get(notificationPanelViewController);
    }

    public static Provider<LockscreenShadeKeyguardTransitionController.Factory> create(C0001LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionController_Factory) {
        return InstanceFactory.create(new LockscreenShadeKeyguardTransitionController_Factory_Impl(lockscreenShadeKeyguardTransitionController_Factory));
    }
}
