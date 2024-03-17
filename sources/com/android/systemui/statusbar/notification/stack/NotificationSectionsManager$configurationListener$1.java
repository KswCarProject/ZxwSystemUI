package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.policy.ConfigurationController;

/* compiled from: NotificationSectionsManager.kt */
public final class NotificationSectionsManager$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    public final /* synthetic */ NotificationSectionsManager this$0;

    public NotificationSectionsManager$configurationListener$1(NotificationSectionsManager notificationSectionsManager) {
        this.this$0 = notificationSectionsManager;
    }

    public void onLocaleListChanged() {
        this.this$0.reinflateViews();
    }
}
