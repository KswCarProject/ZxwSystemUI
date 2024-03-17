package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.statusbar.CommandQueue;

public class TvNotificationPanel extends CoreStartable implements CommandQueue.Callbacks {
    public final CommandQueue mCommandQueue;
    public final String mNotificationHandlerPackage = this.mContext.getResources().getString(17040010);

    public TvNotificationPanel(Context context, CommandQueue commandQueue) {
        super(context);
        this.mCommandQueue = commandQueue;
    }

    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    public void togglePanel() {
        if (!this.mNotificationHandlerPackage.isEmpty()) {
            startNotificationHandlerActivity(new Intent("android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL"));
        } else {
            openInternalNotificationPanel("android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL");
        }
    }

    public void animateExpandNotificationsPanel() {
        if (!this.mNotificationHandlerPackage.isEmpty()) {
            startNotificationHandlerActivity(new Intent("android.app.action.OPEN_NOTIFICATION_HANDLER_PANEL"));
        } else {
            openInternalNotificationPanel("android.app.action.OPEN_NOTIFICATION_HANDLER_PANEL");
        }
    }

    public void animateCollapsePanels(int i, boolean z) {
        if (this.mNotificationHandlerPackage.isEmpty() || (i & 4) != 0) {
            openInternalNotificationPanel("android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL");
            return;
        }
        Intent intent = new Intent("android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL");
        intent.setPackage(this.mNotificationHandlerPackage);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    public final void openInternalNotificationPanel(String str) {
        Intent intent = new Intent(this.mContext, TvNotificationPanelActivity.class);
        intent.setFlags(603979776);
        intent.setAction(str);
        this.mContext.startActivityAsUser(intent, UserHandle.SYSTEM);
    }

    public final void startNotificationHandlerActivity(Intent intent) {
        ActivityInfo activityInfo;
        intent.setPackage(this.mNotificationHandlerPackage);
        ResolveInfo resolveActivity = this.mContext.getPackageManager().resolveActivity(intent, 1048576);
        if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null) {
            Log.e("TvNotificationPanel", "Not launching notification handler activity: Could not resolve activityInfo for intent " + intent.getAction());
            return;
        }
        String str = activityInfo.permission;
        if (str == null || !str.equals("android.permission.STATUS_BAR_SERVICE")) {
            Log.e("TvNotificationPanel", "Not launching notification handler activity: Notification handler does not require the STATUS_BAR_SERVICE permission for intent " + intent.getAction());
            return;
        }
        intent.setFlags(603979776);
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
    }
}
