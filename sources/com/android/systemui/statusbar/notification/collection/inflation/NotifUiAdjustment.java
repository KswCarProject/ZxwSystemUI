package com.android.systemui.statusbar.notification.collection.inflation;

import android.app.Notification;
import android.app.RemoteInput;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifUiAdjustment.kt */
public final class NotifUiAdjustment {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public final boolean isConversation;
    public final boolean isMinimized;
    @NotNull
    public final String key;
    public final boolean needsRedaction;
    @NotNull
    public final List<Notification.Action> smartActions;
    @NotNull
    public final List<CharSequence> smartReplies;

    public static final boolean needReinflate(@NotNull NotifUiAdjustment notifUiAdjustment, @NotNull NotifUiAdjustment notifUiAdjustment2) {
        return Companion.needReinflate(notifUiAdjustment, notifUiAdjustment2);
    }

    public NotifUiAdjustment(@NotNull String str, @NotNull List<? extends Notification.Action> list, @NotNull List<? extends CharSequence> list2, boolean z, boolean z2, boolean z3) {
        this.key = str;
        this.smartActions = list;
        this.smartReplies = list2;
        this.isConversation = z;
        this.isMinimized = z2;
        this.needsRedaction = z3;
    }

    @NotNull
    public final List<Notification.Action> getSmartActions() {
        return this.smartActions;
    }

    @NotNull
    public final List<CharSequence> getSmartReplies() {
        return this.smartReplies;
    }

    public final boolean isConversation() {
        return this.isConversation;
    }

    public final boolean isMinimized() {
        return this.isMinimized;
    }

    public final boolean getNeedsRedaction() {
        return this.needsRedaction;
    }

    /* compiled from: NotifUiAdjustment.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final boolean needReinflate(@NotNull NotifUiAdjustment notifUiAdjustment, @NotNull NotifUiAdjustment notifUiAdjustment2) {
            if (notifUiAdjustment == notifUiAdjustment2) {
                return false;
            }
            return (notifUiAdjustment.isConversation() == notifUiAdjustment2.isConversation() && notifUiAdjustment.isMinimized() == notifUiAdjustment2.isMinimized() && notifUiAdjustment.getNeedsRedaction() == notifUiAdjustment2.getNeedsRedaction() && !areDifferent((List<? extends Notification.Action>) notifUiAdjustment.getSmartActions(), (List<? extends Notification.Action>) notifUiAdjustment2.getSmartActions()) && Intrinsics.areEqual((Object) notifUiAdjustment2.getSmartReplies(), (Object) notifUiAdjustment.getSmartReplies())) ? false : true;
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x009b A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean areDifferent(java.util.List<? extends android.app.Notification.Action> r5, java.util.List<? extends android.app.Notification.Action> r6) {
            /*
                r4 = this;
                r4 = 1
                r0 = 0
                if (r5 != r6) goto L_0x0007
            L_0x0004:
                r4 = r0
                goto L_0x009b
            L_0x0007:
                int r1 = r5.size()
                int r2 = r6.size()
                if (r1 == r2) goto L_0x0013
                goto L_0x009b
            L_0x0013:
                java.lang.Iterable r5 = (java.lang.Iterable) r5
                kotlin.sequences.Sequence r5 = kotlin.collections.CollectionsKt___CollectionsKt.asSequence(r5)
                java.lang.Iterable r6 = (java.lang.Iterable) r6
                kotlin.sequences.Sequence r6 = kotlin.collections.CollectionsKt___CollectionsKt.asSequence(r6)
                kotlin.sequences.Sequence r5 = kotlin.sequences.SequencesKt___SequencesKt.zip(r5, r6)
                java.util.Iterator r5 = r5.iterator()
            L_0x0027:
                boolean r6 = r5.hasNext()
                if (r6 == 0) goto L_0x0004
                java.lang.Object r6 = r5.next()
                kotlin.Pair r6 = (kotlin.Pair) r6
                java.lang.Object r1 = r6.getFirst()
                android.app.Notification$Action r1 = (android.app.Notification.Action) r1
                java.lang.CharSequence r1 = r1.title
                java.lang.Object r2 = r6.getSecond()
                android.app.Notification$Action r2 = (android.app.Notification.Action) r2
                java.lang.CharSequence r2 = r2.title
                boolean r1 = android.text.TextUtils.equals(r1, r2)
                if (r1 == 0) goto L_0x0098
                com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment$Companion r1 = com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment.Companion
                java.lang.Object r2 = r6.getFirst()
                android.app.Notification$Action r2 = (android.app.Notification.Action) r2
                android.graphics.drawable.Icon r2 = r2.getIcon()
                java.lang.Object r3 = r6.getSecond()
                android.app.Notification$Action r3 = (android.app.Notification.Action) r3
                android.graphics.drawable.Icon r3 = r3.getIcon()
                boolean r2 = r1.areDifferent((android.graphics.drawable.Icon) r2, (android.graphics.drawable.Icon) r3)
                if (r2 != 0) goto L_0x0098
                java.lang.Object r2 = r6.getFirst()
                android.app.Notification$Action r2 = (android.app.Notification.Action) r2
                android.app.PendingIntent r2 = r2.actionIntent
                java.lang.Object r3 = r6.getSecond()
                android.app.Notification$Action r3 = (android.app.Notification.Action) r3
                android.app.PendingIntent r3 = r3.actionIntent
                boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r3)
                if (r2 == 0) goto L_0x0098
                java.lang.Object r2 = r6.getFirst()
                android.app.Notification$Action r2 = (android.app.Notification.Action) r2
                android.app.RemoteInput[] r2 = r2.getRemoteInputs()
                java.lang.Object r6 = r6.getSecond()
                android.app.Notification$Action r6 = (android.app.Notification.Action) r6
                android.app.RemoteInput[] r6 = r6.getRemoteInputs()
                boolean r6 = r1.areDifferent((android.app.RemoteInput[]) r2, (android.app.RemoteInput[]) r6)
                if (r6 == 0) goto L_0x0096
                goto L_0x0098
            L_0x0096:
                r6 = r0
                goto L_0x0099
            L_0x0098:
                r6 = r4
            L_0x0099:
                if (r6 == 0) goto L_0x0027
            L_0x009b:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment.Companion.areDifferent(java.util.List, java.util.List):boolean");
        }

        public final boolean areDifferent(Icon icon, Icon icon2) {
            return icon != icon2 && (icon == null || icon2 == null || !icon.sameAs(icon2));
        }

        public final boolean areDifferent(RemoteInput[] remoteInputArr, RemoteInput[] remoteInputArr2) {
            boolean z;
            if (remoteInputArr == remoteInputArr2) {
                return false;
            }
            if (remoteInputArr == null || remoteInputArr2 == null || remoteInputArr.length != remoteInputArr2.length) {
                return true;
            }
            for (Pair pair : SequencesKt___SequencesKt.zip(ArraysKt___ArraysKt.asSequence(remoteInputArr), ArraysKt___ArraysKt.asSequence(remoteInputArr2))) {
                if (!TextUtils.equals(((RemoteInput) pair.getFirst()).getLabel(), ((RemoteInput) pair.getSecond()).getLabel()) || NotifUiAdjustment.Companion.areDifferent(((RemoteInput) pair.getFirst()).getChoices(), ((RemoteInput) pair.getSecond()).getChoices())) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    return true;
                }
            }
            return false;
        }

        public final boolean areDifferent(CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2) {
            if (charSequenceArr == charSequenceArr2) {
                return false;
            }
            if (charSequenceArr == null || charSequenceArr2 == null || charSequenceArr.length != charSequenceArr2.length) {
                return true;
            }
            for (Pair pair : SequencesKt___SequencesKt.zip(ArraysKt___ArraysKt.asSequence(charSequenceArr), ArraysKt___ArraysKt.asSequence(charSequenceArr2))) {
                if (!TextUtils.equals((CharSequence) pair.getFirst(), (CharSequence) pair.getSecond())) {
                    return true;
                }
            }
            return false;
        }
    }
}
