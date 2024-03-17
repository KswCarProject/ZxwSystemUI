package com.android.wm.shell.bubbles;

import android.app.NotificationChannel;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.util.ArraySet;
import android.util.Pair;
import android.util.SparseArray;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface Bubbles {

    public interface BubbleExpandListener {
        void onBubbleExpandChanged(boolean z, String str);
    }

    public interface BubbleMetadataFlagListener {
        void onBubbleMetadataFlagChanged(Bubble bubble);
    }

    public interface PendingIntentCanceledListener {
        void onPendingIntentCanceled(Bubble bubble);
    }

    public interface SysuiProxy {
        void getPendingOrActiveEntry(String str, Consumer<BubbleEntry> consumer);

        void getShouldRestoredEntries(ArraySet<String> arraySet, Consumer<List<BubbleEntry>> consumer);

        void notifyInvalidateNotifications(String str);

        void notifyMaybeCancelSummary(String str);

        void notifyRemoveNotification(String str, int i);

        void onManageMenuExpandChanged(boolean z);

        void onStackExpandChanged(boolean z);

        void onUnbubbleConversation(String str);

        void removeNotificationEntry(String str);

        void requestNotificationShadeTopUi(boolean z, String str);

        void setNotificationInterruption(String str);

        void updateNotificationBubbleButton(String str);

        void updateNotificationSuppression(String str);
    }

    void collapseStack();

    void dump(PrintWriter printWriter, String[] strArr);

    void expandStackAndSelectBubble(Bubble bubble);

    void expandStackAndSelectBubble(BubbleEntry bubbleEntry);

    Bubble getBubbleWithShortcutId(String str);

    boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer, Executor executor);

    boolean isBubbleExpanded(String str);

    boolean isBubbleNotificationSuppressedFromShade(String str, String str2);

    void onConfigChanged(Configuration configuration);

    void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray);

    void onEntryAdded(BubbleEntry bubbleEntry);

    void onEntryRemoved(BubbleEntry bubbleEntry);

    void onEntryUpdated(BubbleEntry bubbleEntry, boolean z);

    void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i);

    void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap);

    void onStatusBarStateChanged(boolean z);

    void onStatusBarVisibilityChanged(boolean z);

    void onUserChanged(int i);

    void onUserRemoved(int i);

    void onZenStateChanged();

    void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer, Executor executor);

    void setExpandListener(BubbleExpandListener bubbleExpandListener);

    void setSysuiProxy(SysuiProxy sysuiProxy);

    void updateForThemeChanges();
}
