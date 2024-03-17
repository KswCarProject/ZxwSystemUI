package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager {
    @NotNull
    public final BindEventManager bindEventManager;
    @NotNull
    public final HeadsUpManager headsUpManager;
    public boolean isStatusBarExpanded;
    @NotNull
    public final CommonNotifCollection notifCollection;
    @NotNull
    public final StatusBarStateController statusBarStateController;

    public AnimatedImageNotificationManager(@NotNull CommonNotifCollection commonNotifCollection, @NotNull BindEventManager bindEventManager2, @NotNull HeadsUpManager headsUpManager2, @NotNull StatusBarStateController statusBarStateController2) {
        this.notifCollection = commonNotifCollection;
        this.bindEventManager = bindEventManager2;
        this.headsUpManager = headsUpManager2;
        this.statusBarStateController = statusBarStateController2;
    }

    public final void bind() {
        this.headsUpManager.addListener(new AnimatedImageNotificationManager$bind$1(this));
        this.statusBarStateController.addCallback(new AnimatedImageNotificationManager$bind$2(this));
        this.bindEventManager.addListener(new AnimatedImageNotificationManager$bind$3(this));
    }

    public final Unit updateAnimatedImageDrawables(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row == null) {
            return null;
        }
        updateAnimatedImageDrawables(row, row.isHeadsUp() || this.isStatusBarExpanded);
        return Unit.INSTANCE;
    }

    public final void updateAnimatedImageDrawables(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        Sequence asSequence = layouts == null ? null : ArraysKt___ArraysKt.asSequence(layouts);
        if (asSequence == null) {
            asSequence = SequencesKt__SequencesKt.emptySequence();
        }
        for (AnimatedImageDrawable animatedImageDrawable : SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(SequencesKt___SequencesKt.flatMap(SequencesKt___SequencesKt.flatMap(asSequence, AnimatedImageNotificationManager$updateAnimatedImageDrawables$2.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$3.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$4.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$5.INSTANCE)) {
            if (z) {
                animatedImageDrawable.start();
            } else {
                animatedImageDrawable.stop();
            }
        }
    }
}
