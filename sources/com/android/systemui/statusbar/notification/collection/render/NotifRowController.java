package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.FeedbackIcon;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifRowController.kt */
public interface NotifRowController {
    void setFeedbackIcon(@Nullable FeedbackIcon feedbackIcon);

    void setLastAudiblyAlertedMs(long j);

    void setSystemExpanded(boolean z);
}
