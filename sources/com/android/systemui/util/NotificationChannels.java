package com.android.systemui.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$string;
import java.util.Arrays;

public class NotificationChannels extends CoreStartable {
    public static String ALERTS = "ALR";
    public static String BATTERY = "BAT";
    public static String GENERAL = "GEN";
    public static String HINTS = "HNT";
    public static String SCREENSHOTS_HEADSUP = "SCN_HEADSUP";
    public static String STORAGE = "DSK";
    public static String TVPIP = "TVPIP";

    public NotificationChannels(Context context) {
        super(context);
    }

    public static void createAll(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel(BATTERY, context.getString(R$string.notification_channel_battery), 5);
        String string = Settings.Global.getString(context.getContentResolver(), "low_battery_sound");
        notificationChannel.setSound(Uri.parse("file://" + string), new AudioAttributes.Builder().setContentType(4).setUsage(10).build());
        notificationChannel.setBlockable(true);
        notificationManager.createNotificationChannels(Arrays.asList(new NotificationChannel[]{new NotificationChannel(ALERTS, context.getString(R$string.notification_channel_alerts), 4), new NotificationChannel(GENERAL, context.getString(R$string.notification_channel_general), 1), new NotificationChannel(STORAGE, context.getString(R$string.notification_channel_storage), isTv(context) ? 3 : 2), createScreenshotChannel(context.getString(R$string.notification_channel_screenshot)), notificationChannel, new NotificationChannel(HINTS, context.getString(R$string.notification_channel_hints), 3)}));
        if (isTv(context)) {
            notificationManager.createNotificationChannel(new NotificationChannel(TVPIP, context.getString(R$string.notification_channel_tv_pip), 5));
        }
    }

    @VisibleForTesting
    public static NotificationChannel createScreenshotChannel(String str) {
        NotificationChannel notificationChannel = new NotificationChannel(SCREENSHOTS_HEADSUP, str, 4);
        notificationChannel.setSound((Uri) null, new AudioAttributes.Builder().setUsage(5).build());
        notificationChannel.setBlockable(true);
        return notificationChannel;
    }

    public void start() {
        createAll(this.mContext);
    }

    public static boolean isTv(Context context) {
        return context.getPackageManager().hasSystemFeature("android.software.leanback");
    }
}
