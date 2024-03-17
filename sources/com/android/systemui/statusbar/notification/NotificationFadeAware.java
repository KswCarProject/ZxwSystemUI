package com.android.systemui.statusbar.notification;

import android.graphics.Paint;
import android.view.View;

public interface NotificationFadeAware {

    public interface FadeOptimizedNotification extends NotificationFadeAware {
        boolean isNotificationFaded();
    }

    void setNotificationFaded(boolean z);

    static void setLayerTypeForFaded(View view, boolean z) {
        if (view != null) {
            view.setLayerType(z ? 2 : 0, (Paint) null);
        }
    }
}
