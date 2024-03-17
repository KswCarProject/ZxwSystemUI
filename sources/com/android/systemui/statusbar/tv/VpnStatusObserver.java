package com.android.systemui.statusbar.tv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import com.android.internal.net.VpnConfig;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.policy.SecurityController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: VpnStatusObserver.kt */
public final class VpnStatusObserver extends CoreStartable implements SecurityController.SecurityControllerCallback {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final String NOTIFICATION_TAG = VpnStatusObserver.class.getSimpleName();
    @NotNull
    public final NotificationChannel notificationChannel = createNotificationChannel();
    public final NotificationManager notificationManager;
    @NotNull
    public final SecurityController securityController;
    public boolean vpnConnected;
    public final Notification.Builder vpnConnectedNotificationBuilder = createVpnConnectedNotificationBuilder();
    public final Notification vpnDisconnectedNotification = createVpnDisconnectedNotification();

    public VpnStatusObserver(@NotNull Context context, @NotNull SecurityController securityController2) {
        super(context);
        this.securityController = securityController2;
        this.notificationManager = NotificationManager.from(context);
    }

    public final int getVpnIconId() {
        if (this.securityController.isVpnBranded()) {
            return R$drawable.stat_sys_branded_vpn;
        }
        return R$drawable.stat_sys_vpn_ic;
    }

    public final String getVpnName() {
        String primaryVpnName = this.securityController.getPrimaryVpnName();
        return primaryVpnName == null ? this.securityController.getWorkProfileVpnName() : primaryVpnName;
    }

    public void start() {
        this.securityController.addCallback(this);
    }

    public void onStateChanged() {
        boolean isVpnEnabled = this.securityController.isVpnEnabled();
        if (this.vpnConnected != isVpnEnabled) {
            if (isVpnEnabled) {
                notifyVpnConnected();
            } else {
                notifyVpnDisconnected();
            }
            this.vpnConnected = isVpnEnabled;
        }
    }

    public final void notifyVpnConnected() {
        this.notificationManager.notify(NOTIFICATION_TAG, 20, createVpnConnectedNotification());
    }

    public final void notifyVpnDisconnected() {
        NotificationManager notificationManager2 = this.notificationManager;
        String str = NOTIFICATION_TAG;
        notificationManager2.cancel(str, 20);
        notificationManager2.notify(str, 17, this.vpnDisconnectedNotification);
    }

    public final NotificationChannel createNotificationChannel() {
        NotificationChannel notificationChannel2 = new NotificationChannel("VPN Status", "VPN Status", 4);
        this.notificationManager.createNotificationChannel(notificationChannel2);
        return notificationChannel2;
    }

    public final Notification createVpnConnectedNotification() {
        Notification.Builder builder = this.vpnConnectedNotificationBuilder;
        String vpnName = getVpnName();
        if (vpnName != null) {
            builder.setContentText(this.mContext.getString(R$string.notification_disclosure_vpn_text, new Object[]{vpnName}));
        }
        return builder.build();
    }

    public final Notification.Builder createVpnConnectedNotificationBuilder() {
        return new Notification.Builder(this.mContext, "VPN Status").setSmallIcon(getVpnIconId()).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender()).setOngoing(true).setContentTitle(this.mContext.getString(R$string.notification_vpn_connected)).setContentIntent(VpnConfig.getIntentForStatusPanel(this.mContext));
    }

    public final Notification createVpnDisconnectedNotification() {
        return new Notification.Builder(this.mContext, "VPN Status").setSmallIcon(getVpnIconId()).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender()).setTimeoutAfter(5000).setContentTitle(this.mContext.getString(R$string.notification_vpn_disconnected)).build();
    }

    /* compiled from: VpnStatusObserver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
