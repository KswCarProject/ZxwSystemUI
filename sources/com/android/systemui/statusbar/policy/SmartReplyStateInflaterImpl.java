package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyStateInflaterImpl implements SmartReplyStateInflater {
    @NotNull
    public final ActivityManagerWrapper activityManagerWrapper;
    @NotNull
    public final SmartReplyConstants constants;
    @NotNull
    public final DevicePolicyManagerWrapper devicePolicyManagerWrapper;
    @NotNull
    public final PackageManagerWrapper packageManagerWrapper;
    @NotNull
    public final SmartActionInflater smartActionsInflater;
    @NotNull
    public final SmartReplyInflater smartRepliesInflater;

    public SmartReplyStateInflaterImpl(@NotNull SmartReplyConstants smartReplyConstants, @NotNull ActivityManagerWrapper activityManagerWrapper2, @NotNull PackageManagerWrapper packageManagerWrapper2, @NotNull DevicePolicyManagerWrapper devicePolicyManagerWrapper2, @NotNull SmartReplyInflater smartReplyInflater, @NotNull SmartActionInflater smartActionInflater) {
        this.constants = smartReplyConstants;
        this.activityManagerWrapper = activityManagerWrapper2;
        this.packageManagerWrapper = packageManagerWrapper2;
        this.devicePolicyManagerWrapper = devicePolicyManagerWrapper2;
        this.smartRepliesInflater = smartReplyInflater;
        this.smartActionsInflater = smartActionInflater;
    }

    @NotNull
    public InflatedSmartReplyState inflateSmartReplyState(@NotNull NotificationEntry notificationEntry) {
        return chooseSmartRepliesAndActions(notificationEntry);
    }

    @NotNull
    public InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(@NotNull Context context, @NotNull Context context2, @NotNull NotificationEntry notificationEntry, @Nullable InflatedSmartReplyState inflatedSmartReplyState, @NotNull InflatedSmartReplyState inflatedSmartReplyState2) {
        boolean z;
        Sequence sequence;
        Sequence sequence2 = null;
        if (!SmartReplyStateInflaterKt.shouldShowSmartReplyView(notificationEntry, inflatedSmartReplyState2)) {
            return new InflatedSmartReplyViewHolder((SmartReplyView) null, (List<? extends Button>) null);
        }
        boolean z2 = !SmartReplyStateInflaterKt.areSuggestionsSimilar(inflatedSmartReplyState, inflatedSmartReplyState2);
        SmartReplyView inflate = SmartReplyView.inflate(context, this.constants);
        SmartReplyView.SmartReplies smartReplies = inflatedSmartReplyState2.getSmartReplies();
        if (smartReplies == null) {
            z = false;
        } else {
            z = smartReplies.fromAssistant;
        }
        inflate.setSmartRepliesGeneratedByAssistant(z);
        if (smartReplies == null) {
            sequence = null;
        } else {
            sequence = SequencesKt___SequencesKt.mapIndexed(CollectionsKt___CollectionsKt.asSequence(smartReplies.choices), new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1(this, inflate, notificationEntry, smartReplies, z2));
        }
        if (sequence == null) {
            sequence = SequencesKt__SequencesKt.emptySequence();
        }
        Sequence sequence3 = sequence;
        SmartReplyView.SmartActions smartActions = inflatedSmartReplyState2.getSmartActions();
        if (smartActions != null) {
            sequence2 = SequencesKt___SequencesKt.mapIndexed(SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(smartActions.actions), SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1.INSTANCE), new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2(this, inflate, notificationEntry, smartActions, z2, new ContextThemeWrapper(context2, context.getTheme())));
        }
        if (sequence2 == null) {
            sequence2 = SequencesKt__SequencesKt.emptySequence();
        }
        return new InflatedSmartReplyViewHolder(inflate, SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.plus(sequence3, sequence2)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x0168 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x015c  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0161  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0165  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x00f9 A[EDGE_INSN: B:95:0x00f9->B:54:0x00f9 ?: BREAK  , SYNTHETIC] */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.policy.InflatedSmartReplyState chooseSmartRepliesAndActions(@org.jetbrains.annotations.NotNull com.android.systemui.statusbar.notification.collection.NotificationEntry r12) {
        /*
            r11 = this;
            android.service.notification.StatusBarNotification r0 = r12.getSbn()
            android.app.Notification r0 = r0.getNotification()
            r1 = 0
            android.util.Pair r2 = r0.findRemoteInputActionPair(r1)
            r3 = 1
            android.util.Pair r4 = r0.findRemoteInputActionPair(r3)
            com.android.systemui.statusbar.policy.SmartReplyConstants r5 = r11.constants
            boolean r5 = r5.isEnabled()
            r6 = 0
            if (r5 != 0) goto L_0x003a
            boolean r11 = com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt.DEBUG
            if (r11 == 0) goto L_0x0034
            android.service.notification.StatusBarNotification r11 = r12.getSbn()
            java.lang.String r11 = r11.getKey()
            java.lang.String r12 = "Smart suggestions not enabled, not adding suggestions for "
            java.lang.String r11 = kotlin.jvm.internal.Intrinsics.stringPlus(r12, r11)
            java.lang.String r12 = "SmartReplyViewInflater"
            android.util.Log.d(r12, r11)
        L_0x0034:
            com.android.systemui.statusbar.policy.InflatedSmartReplyState r11 = new com.android.systemui.statusbar.policy.InflatedSmartReplyState
            r11.<init>(r6, r6, r6, r1)
            return r11
        L_0x003a:
            com.android.systemui.statusbar.policy.SmartReplyConstants r5 = r11.constants
            boolean r5 = r5.requiresTargetingP()
            if (r5 == 0) goto L_0x004b
            int r5 = r12.targetSdk
            r7 = 28
            if (r5 < r7) goto L_0x0049
            goto L_0x004b
        L_0x0049:
            r5 = r1
            goto L_0x004c
        L_0x004b:
            r5 = r3
        L_0x004c:
            java.util.List r7 = r0.getContextualActions()
            if (r5 == 0) goto L_0x008c
            if (r2 != 0) goto L_0x0055
            goto L_0x008c
        L_0x0055:
            java.lang.Object r5 = r2.second
            android.app.Notification$Action r5 = (android.app.Notification.Action) r5
            android.app.PendingIntent r5 = r5.actionIntent
            if (r5 != 0) goto L_0x005e
            goto L_0x008c
        L_0x005e:
            java.lang.Object r8 = r2.first
            android.app.RemoteInput r8 = (android.app.RemoteInput) r8
            java.lang.CharSequence[] r8 = r8.getChoices()
            if (r8 != 0) goto L_0x006a
        L_0x0068:
            r8 = r1
            goto L_0x0074
        L_0x006a:
            int r8 = r8.length
            if (r8 != 0) goto L_0x006f
            r8 = r3
            goto L_0x0070
        L_0x006f:
            r8 = r1
        L_0x0070:
            r8 = r8 ^ r3
            if (r8 != r3) goto L_0x0068
            r8 = r3
        L_0x0074:
            if (r8 == 0) goto L_0x008c
            com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies r8 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies
            java.lang.Object r9 = r2.first
            android.app.RemoteInput r9 = (android.app.RemoteInput) r9
            java.lang.CharSequence[] r9 = r9.getChoices()
            java.util.List r9 = kotlin.collections.ArraysKt___ArraysJvmKt.asList(r9)
            java.lang.Object r2 = r2.first
            android.app.RemoteInput r2 = (android.app.RemoteInput) r2
            r8.<init>(r9, r2, r5, r1)
            goto L_0x008d
        L_0x008c:
            r8 = r6
        L_0x008d:
            r2 = r7
            java.util.Collection r2 = (java.util.Collection) r2
            boolean r2 = r2.isEmpty()
            r2 = r2 ^ r3
            if (r2 == 0) goto L_0x009d
            com.android.systemui.statusbar.policy.SmartReplyView$SmartActions r2 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartActions
            r2.<init>(r7, r1)
            goto L_0x009e
        L_0x009d:
            r2 = r6
        L_0x009e:
            if (r8 != 0) goto L_0x00f7
            if (r2 != 0) goto L_0x00f7
            java.util.List r5 = r12.getSmartReplies()
            java.util.List r12 = r12.getSmartActions()
            r7 = r5
            java.util.Collection r7 = (java.util.Collection) r7
            boolean r7 = r7.isEmpty()
            r7 = r7 ^ r3
            if (r7 == 0) goto L_0x00d6
            if (r4 == 0) goto L_0x00d6
            java.lang.Object r7 = r4.second
            android.app.Notification$Action r7 = (android.app.Notification.Action) r7
            boolean r7 = r7.getAllowGeneratedReplies()
            if (r7 == 0) goto L_0x00d6
            java.lang.Object r7 = r4.second
            r9 = r7
            android.app.Notification$Action r9 = (android.app.Notification.Action) r9
            android.app.PendingIntent r9 = r9.actionIntent
            if (r9 == 0) goto L_0x00d6
            com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies r8 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies
            java.lang.Object r4 = r4.first
            android.app.RemoteInput r4 = (android.app.RemoteInput) r4
            android.app.Notification$Action r7 = (android.app.Notification.Action) r7
            android.app.PendingIntent r7 = r7.actionIntent
            r8.<init>(r5, r4, r7, r3)
        L_0x00d6:
            r4 = r12
            java.util.Collection r4 = (java.util.Collection) r4
            boolean r4 = r4.isEmpty()
            r4 = r4 ^ r3
            if (r4 == 0) goto L_0x00f7
            boolean r4 = r0.getAllowSystemGeneratedContextualActions()
            if (r4 == 0) goto L_0x00f7
            com.android.systemui.shared.system.ActivityManagerWrapper r2 = r11.activityManagerWrapper
            boolean r2 = r2.isLockTaskKioskModeActive()
            if (r2 == 0) goto L_0x00f2
            java.util.List r12 = r11.filterAllowlistedLockTaskApps(r12)
        L_0x00f2:
            com.android.systemui.statusbar.policy.SmartReplyView$SmartActions r2 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartActions
            r2.<init>(r12, r3)
        L_0x00f7:
            if (r2 != 0) goto L_0x00fb
        L_0x00f9:
            r11 = r1
            goto L_0x0134
        L_0x00fb:
            java.util.List<android.app.Notification$Action> r11 = r2.actions
            if (r11 != 0) goto L_0x0100
            goto L_0x00f9
        L_0x0100:
            java.lang.Iterable r11 = (java.lang.Iterable) r11
            boolean r12 = r11 instanceof java.util.Collection
            if (r12 == 0) goto L_0x0110
            r12 = r11
            java.util.Collection r12 = (java.util.Collection) r12
            boolean r12 = r12.isEmpty()
            if (r12 == 0) goto L_0x0110
            goto L_0x00f9
        L_0x0110:
            java.util.Iterator r11 = r11.iterator()
        L_0x0114:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L_0x00f9
            java.lang.Object r12 = r11.next()
            android.app.Notification$Action r12 = (android.app.Notification.Action) r12
            boolean r4 = r12.isContextual()
            if (r4 == 0) goto L_0x0130
            int r12 = r12.getSemanticAction()
            r4 = 12
            if (r12 != r4) goto L_0x0130
            r12 = r3
            goto L_0x0131
        L_0x0130:
            r12 = r1
        L_0x0131:
            if (r12 == 0) goto L_0x0114
            r11 = r3
        L_0x0134:
            if (r11 == 0) goto L_0x016f
            android.app.Notification$Action[] r12 = r0.actions
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            int r4 = r12.length
            r5 = r1
            r7 = r5
        L_0x0140:
            if (r5 >= r4) goto L_0x016a
            r9 = r12[r5]
            int r5 = r5 + 1
            int r10 = r7 + 1
            android.app.RemoteInput[] r9 = r9.getRemoteInputs()
            if (r9 != 0) goto L_0x0150
        L_0x014e:
            r9 = r1
            goto L_0x015a
        L_0x0150:
            int r9 = r9.length
            if (r9 != 0) goto L_0x0155
            r9 = r3
            goto L_0x0156
        L_0x0155:
            r9 = r1
        L_0x0156:
            r9 = r9 ^ r3
            if (r9 != r3) goto L_0x014e
            r9 = r3
        L_0x015a:
            if (r9 == 0) goto L_0x0161
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            goto L_0x0162
        L_0x0161:
            r7 = r6
        L_0x0162:
            if (r7 != 0) goto L_0x0165
            goto L_0x0168
        L_0x0165:
            r0.add(r7)
        L_0x0168:
            r7 = r10
            goto L_0x0140
        L_0x016a:
            com.android.systemui.statusbar.policy.InflatedSmartReplyState$SuppressedActions r6 = new com.android.systemui.statusbar.policy.InflatedSmartReplyState$SuppressedActions
            r6.<init>(r0)
        L_0x016f:
            com.android.systemui.statusbar.policy.InflatedSmartReplyState r12 = new com.android.systemui.statusbar.policy.InflatedSmartReplyState
            r12.<init>(r8, r2, r6, r11)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl.chooseSmartRepliesAndActions(com.android.systemui.statusbar.notification.collection.NotificationEntry):com.android.systemui.statusbar.policy.InflatedSmartReplyState");
    }

    public final List<Notification.Action> filterAllowlistedLockTaskApps(List<? extends Notification.Action> list) {
        Intent intent;
        ResolveInfo resolveActivity;
        ArrayList arrayList = new ArrayList();
        for (Object next : list) {
            PendingIntent pendingIntent = ((Notification.Action) next).actionIntent;
            boolean z = false;
            if (!(pendingIntent == null || (intent = pendingIntent.getIntent()) == null || (resolveActivity = this.packageManagerWrapper.resolveActivity(intent, 0)) == null)) {
                z = this.devicePolicyManagerWrapper.isLockTaskPermitted(resolveActivity.activityInfo.packageName);
            }
            if (z) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }
}
