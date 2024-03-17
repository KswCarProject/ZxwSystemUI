package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;

public class QsFrameTranslateImpl extends QsFrameTranslateController {
    public float getNotificationsTopPadding(float f, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return f;
    }

    public void translateQsFrame(View view, QS qs, float f, float f2) {
    }

    public QsFrameTranslateImpl(CentralSurfaces centralSurfaces) {
        super(centralSurfaces);
    }
}
