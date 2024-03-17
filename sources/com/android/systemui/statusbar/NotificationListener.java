package com.android.systemui.statusbar;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.time.SystemClock;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;

@SuppressLint({"OverrideAbstract"})
public class NotificationListener extends NotificationListenerWithPlugins {
    public final Context mContext;
    public final Runnable mDispatchRankingUpdateRunnable = new NotificationListener$$ExternalSyntheticLambda0(this);
    public final Executor mMainExecutor;
    public final List<NotificationHandler> mNotificationHandlers = new ArrayList();
    public final NotificationManager mNotificationManager;
    public final Deque<NotificationListenerService.RankingMap> mRankingMapQueue = new ConcurrentLinkedDeque();
    public final ArrayList<NotificationSettingsListener> mSettingsListeners = new ArrayList<>();
    public long mSkippingRankingUpdatesSince = -1;
    public final SystemClock mSystemClock;

    public interface NotificationHandler {
        void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        }

        void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap);

        void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap);

        void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i);

        void onNotificationsInitialized();
    }

    public interface NotificationSettingsListener {
        void onStatusBarIconsBehaviorChanged(boolean z) {
        }
    }

    public NotificationListener(Context context, NotificationManager notificationManager, SystemClock systemClock, Executor executor, PluginManager pluginManager) {
        super(pluginManager);
        this.mContext = context;
        this.mNotificationManager = notificationManager;
        this.mSystemClock = systemClock;
        this.mMainExecutor = executor;
    }

    public void addNotificationHandler(NotificationHandler notificationHandler) {
        if (!this.mNotificationHandlers.contains(notificationHandler)) {
            this.mNotificationHandlers.add(notificationHandler);
            return;
        }
        throw new IllegalArgumentException("Listener is already added");
    }

    public void addNotificationSettingsListener(NotificationSettingsListener notificationSettingsListener) {
        this.mSettingsListeners.add(notificationSettingsListener);
    }

    public void onListenerConnected() {
        onPluginConnected();
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        if (activeNotifications == null) {
            Log.w("NotificationListener", "onListenerConnected unable to get active notifications.");
            return;
        }
        this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda3(this, activeNotifications, getCurrentRanking()));
        onSilentStatusBarIconsVisibilityChanged(this.mNotificationManager.shouldHideSilentStatusBarIcons());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onListenerConnected$0(StatusBarNotification[] statusBarNotificationArr, NotificationListenerService.RankingMap rankingMap) {
        ArrayList arrayList = new ArrayList();
        for (StatusBarNotification key : statusBarNotificationArr) {
            arrayList.add(getRankingOrTemporaryStandIn(rankingMap, key.getKey()));
        }
        NotificationListenerService.RankingMap rankingMap2 = new NotificationListenerService.RankingMap((NotificationListenerService.Ranking[]) arrayList.toArray(new NotificationListenerService.Ranking[0]));
        for (StatusBarNotification statusBarNotification : statusBarNotificationArr) {
            String packageName = statusBarNotification.getPackageName();
            Bundle bundle = statusBarNotification.getNotification().extras;
            String string = bundle.getString("android.title");
            String string2 = bundle.getString("android.text");
            long postTime = statusBarNotification.getPostTime();
            Intent intent = new Intent();
            intent.setAction("com.szchoiceway.customerui.service.NotificationMessageService");
            intent.putExtra("title", string);
            intent.putExtra("text", string2);
            intent.putExtra("postTime", postTime);
            intent.putExtra(ResourceEntry.KEY_PACKAGE_NAME, packageName);
            intent.putExtra("id", statusBarNotification.getId());
            this.mContext.sendBroadcast(intent);
            if ((!packageName.equals(ThemeOverlayApplier.ANDROID_PACKAGE) || !statusBarNotification.getNotification().getChannelId().equals("DEVELOPER")) && !packageName.equals("com.zxwtxz.sdkdemo") && !packageName.equals("com.txznet.smartadapter") && !packageName.equals("com.txznet.txz")) {
                for (NotificationHandler onNotificationPosted : this.mNotificationHandlers) {
                    onNotificationPosted.onNotificationPosted(statusBarNotification, rankingMap2);
                }
            }
        }
        for (NotificationHandler onNotificationsInitialized : this.mNotificationHandlers) {
            onNotificationsInitialized.onNotificationsInitialized();
        }
    }

    public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        String packageName = statusBarNotification.getPackageName();
        Bundle bundle = statusBarNotification.getNotification().extras;
        String string = bundle.getString("android.title");
        String string2 = bundle.getString("android.text");
        long postTime = statusBarNotification.getPostTime();
        Intent intent = new Intent();
        intent.setAction("com.szchoiceway.customerui.service.NotificationMessageService");
        intent.putExtra("title", string);
        intent.putExtra("text", string2);
        intent.putExtra("postTime", postTime);
        intent.putExtra(ResourceEntry.KEY_PACKAGE_NAME, packageName);
        intent.putExtra("id", statusBarNotification.getId());
        this.mContext.sendBroadcast(intent);
        if ((!packageName.equals(ThemeOverlayApplier.ANDROID_PACKAGE) || !statusBarNotification.getNotification().getChannelId().equals("DEVELOPER")) && !packageName.equals("com.zxwtxz.sdkdemo") && !packageName.equals("com.txznet.smartadapter") && !packageName.equals("com.txznet.txz") && !onPluginNotificationPosted(statusBarNotification, rankingMap)) {
            this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda2(this, statusBarNotification, rankingMap));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationPosted$1(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        RemoteInputController.processForRemoteInput(statusBarNotification.getNotification(), this.mContext);
        for (NotificationHandler onNotificationPosted : this.mNotificationHandlers) {
            onNotificationPosted.onNotificationPosted(statusBarNotification, rankingMap);
        }
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        Intent intent = new Intent();
        intent.setAction("com.szchoiceway.customerui.service.NotificationMessageService");
        intent.putExtra("removed", true);
        intent.putExtra("id", statusBarNotification.getId());
        intent.putExtra(ResourceEntry.KEY_PACKAGE_NAME, statusBarNotification.getPackageName());
        this.mContext.sendBroadcast(intent);
        if (!onPluginNotificationRemoved(statusBarNotification, rankingMap)) {
            this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda1(this, statusBarNotification, rankingMap, i));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationRemoved$2(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        for (NotificationHandler onNotificationRemoved : this.mNotificationHandlers) {
            onNotificationRemoved.onNotificationRemoved(statusBarNotification, rankingMap, i);
        }
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        onNotificationRemoved(statusBarNotification, rankingMap, 0);
    }

    public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        if (rankingMap != null) {
            this.mRankingMapQueue.addLast(onPluginRankingUpdate(rankingMap));
            this.mMainExecutor.execute(this.mDispatchRankingUpdateRunnable);
        }
    }

    public final void dispatchRankingUpdate() {
        NotificationListenerService.RankingMap pollFirst = this.mRankingMapQueue.pollFirst();
        if (pollFirst == null) {
            Log.wtf("NotificationListener", "mRankingMapQueue was empty!");
        }
        if (!this.mRankingMapQueue.isEmpty()) {
            long elapsedRealtime = this.mSystemClock.elapsedRealtime();
            if (this.mSkippingRankingUpdatesSince == -1) {
                this.mSkippingRankingUpdatesSince = elapsedRealtime;
            }
            if (elapsedRealtime - this.mSkippingRankingUpdatesSince < 500) {
                return;
            }
        }
        this.mSkippingRankingUpdatesSince = -1;
        for (NotificationHandler onNotificationRankingUpdate : this.mNotificationHandlers) {
            onNotificationRankingUpdate.onNotificationRankingUpdate(pollFirst);
        }
    }

    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        if (!onPluginNotificationChannelModified(str, userHandle, notificationChannel, i)) {
            this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda4(this, str, userHandle, notificationChannel, i));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationChannelModified$3(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        for (NotificationHandler onNotificationChannelModified : this.mNotificationHandlers) {
            onNotificationChannelModified.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }
    }

    public void onSilentStatusBarIconsVisibilityChanged(boolean z) {
        Iterator<NotificationSettingsListener> it = this.mSettingsListeners.iterator();
        while (it.hasNext()) {
            it.next().onStatusBarIconsBehaviorChanged(z);
        }
    }

    public void registerAsSystemService() {
        try {
            registerAsSystemService(this.mContext, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()), -1);
        } catch (RemoteException e) {
            Log.e("NotificationListener", "Unable to register notification listener", e);
        }
    }

    public static NotificationListenerService.Ranking getRankingOrTemporaryStandIn(NotificationListenerService.RankingMap rankingMap, String str) {
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        if (rankingMap.getRanking(str, ranking)) {
            return ranking;
        }
        ArrayList arrayList = r0;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = r0;
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = r0;
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = r0;
        ArrayList arrayList8 = new ArrayList();
        NotificationListenerService.Ranking ranking2 = ranking;
        ranking.populate(str, 0, false, 0, 0, 0, (CharSequence) null, (String) null, (NotificationChannel) null, arrayList, arrayList3, false, 0, false, 0, false, arrayList5, arrayList7, false, false, false, (ShortcutInfo) null, 0, false);
        return ranking2;
    }
}
