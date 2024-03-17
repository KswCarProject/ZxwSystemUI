package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;

public class AndroidInternalsModule {
    public LockPatternUtils provideLockPatternUtils(Context context) {
        return new LockPatternUtils(context);
    }

    public MetricsLogger provideMetricsLogger() {
        return new MetricsLogger();
    }

    public NotificationMessagingUtil provideNotificationMessagingUtil(Context context) {
        return new NotificationMessagingUtil(context);
    }

    public static UiEventLogger provideUiEventLogger() {
        return new UiEventLoggerImpl();
    }
}
