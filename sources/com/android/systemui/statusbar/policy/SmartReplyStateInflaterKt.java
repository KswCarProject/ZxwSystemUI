package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyStateInflaterKt {
    public static final boolean DEBUG = Log.isLoggable("SmartReplyViewInflater", 3);

    public static final boolean shouldShowSmartReplyView(@NotNull NotificationEntry notificationEntry, @NotNull InflatedSmartReplyState inflatedSmartReplyState) {
        if ((inflatedSmartReplyState.getSmartReplies() != null || inflatedSmartReplyState.getSmartActions() != null) && !notificationEntry.getSbn().getNotification().extras.getBoolean("android.remoteInputSpinner", false)) {
            return !notificationEntry.getSbn().getNotification().extras.getBoolean("android.hideSmartReplies", false);
        }
        return false;
    }

    public static final boolean areSuggestionsSimilar(@Nullable InflatedSmartReplyState inflatedSmartReplyState, @Nullable InflatedSmartReplyState inflatedSmartReplyState2) {
        if (inflatedSmartReplyState == inflatedSmartReplyState2) {
            return true;
        }
        if (inflatedSmartReplyState == null || inflatedSmartReplyState2 == null || inflatedSmartReplyState.getHasPhishingAction() != inflatedSmartReplyState2.getHasPhishingAction() || !Intrinsics.areEqual((Object) inflatedSmartReplyState.getSmartRepliesList(), (Object) inflatedSmartReplyState2.getSmartRepliesList()) || !Intrinsics.areEqual((Object) inflatedSmartReplyState.getSuppressedActionIndices(), (Object) inflatedSmartReplyState2.getSuppressedActionIndices()) || NotificationUiAdjustment.areDifferent(inflatedSmartReplyState.getSmartActionsList(), inflatedSmartReplyState2.getSmartActionsList())) {
            return false;
        }
        return true;
    }

    public static final void executeWhenUnlocked(KeyguardDismissUtil keyguardDismissUtil, boolean z, Function0<Boolean> function0) {
        keyguardDismissUtil.executeWhenUnlocked(new SmartReplyStateInflaterKt$sam$com_android_systemui_plugins_ActivityStarter_OnDismissAction$0(function0), z, false);
    }

    public static final void startPendingIntentDismissingKeyguard(ActivityStarter activityStarter, PendingIntent pendingIntent, View view, Function0<Unit> function0) {
        activityStarter.startPendingIntentDismissingKeyguard(pendingIntent, (Runnable) new SmartReplyStateInflaterKt$startPendingIntentDismissingKeyguard$1(function0), view);
    }
}
