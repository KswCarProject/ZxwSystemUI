package com.android.systemui.statusbar.phone.dagger;

import com.android.keyguard.LockIconViewController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import java.util.Set;

public interface CentralSurfacesComponent {

    public interface Factory {
        CentralSurfacesComponent create();
    }

    public interface Startable {
        void start();

        void stop();
    }

    CollapsedStatusBarFragment createCollapsedStatusBarFragment();

    AuthRippleController getAuthRippleController();

    NotificationRowBinderImpl.BindRowCallback getBindRowCallback();

    CentralSurfacesCommandQueueCallbacks getCentralSurfacesCommandQueueCallbacks();

    LockIconViewController getLockIconViewController();

    NotificationActivityStarter getNotificationActivityStarter();

    NotificationListContainer getNotificationListContainer();

    NotificationPanelViewController getNotificationPanelViewController();

    NotificationPresenter getNotificationPresenter();

    NotificationShadeWindowView getNotificationShadeWindowView();

    NotificationShadeWindowViewController getNotificationShadeWindowViewController();

    NotificationShelfController getNotificationShelfController();

    NotificationStackScrollLayoutController getNotificationStackScrollLayoutController();

    Set<Startable> getStartables();

    StatusBarHeadsUpChangeListener getStatusBarHeadsUpChangeListener();

    StatusBarInitializer getStatusBarInitializer();
}
