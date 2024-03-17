package com.android.systemui.log.dagger;

import android.content.ContentResolver;
import android.os.Build;
import android.os.Looper;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.LogcatEchoTrackerDebug;
import com.android.systemui.log.LogcatEchoTrackerProd;

public class LogModule {
    public static LogBuffer provideDozeLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("DozeLog", 100);
    }

    public static LogBuffer provideNotificationsLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifLog", 1000, false);
    }

    public static LogBuffer provideNotificationHeadsUpLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifHeadsUpLog", 1000);
    }

    public static LogBuffer provideLSShadeTransitionControllerBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("LSShadeTransitionLog", 50);
    }

    public static LogBuffer provideNotificationSectionLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifSectionLog", 1000, false);
    }

    public static LogBuffer provideNotifInteractionLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifInteractionLog", 50);
    }

    public static LogBuffer provideQuickSettingsLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("QSLog", 500, false);
    }

    public static LogBuffer provideBroadcastDispatcherLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("BroadcastDispatcherLog", 500, false);
    }

    public static LogBuffer provideToastLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("ToastLog", 50);
    }

    public static LogBuffer providePrivacyLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("PrivacyLog", 100);
    }

    public static LogBuffer provideCollapsedSbFragmentLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("CollapsedSbFragmentLog", 20);
    }

    public static LogBuffer provideQSFragmentDisableLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("QSFragmentDisableFlagsLog", 10, false);
    }

    public static LogBuffer provideSwipeAwayGestureLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("SwipeStatusBarAwayLog", 30);
    }

    public static LogBuffer provideMediaTttSenderLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaTttSender", 20);
    }

    public static LogBuffer provideMediaTttReceiverLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaTttReceiver", 20);
    }

    public static LogBuffer provideMediaMuteAwaitLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaMuteAwaitLog", 20);
    }

    public static LogBuffer provideNearbyMediaDevicesLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NearbyMediaDevicesLog", 20);
    }

    public static LogBuffer provideMediaViewLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaView", 100);
    }

    public static LogBuffer providesMediaTimeoutListenerLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaTimeout", 100);
    }

    public static LogBuffer provideMediaBrowserBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaBrowser", 100);
    }

    public static LogBuffer provideMediaCarouselControllerBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaCarouselCtlrLog", 20);
    }

    public static LogcatEchoTracker provideLogcatEchoTracker(ContentResolver contentResolver, Looper looper) {
        if (Build.isDebuggable()) {
            return LogcatEchoTrackerDebug.create(contentResolver, looper);
        }
        return new LogcatEchoTrackerProd();
    }
}
