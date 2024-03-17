package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialogController$getDisplayableChannels$channels$1 extends Lambda implements Function1<NotificationChannelGroup, Sequence<? extends NotificationChannel>> {
    public static final ChannelEditorDialogController$getDisplayableChannels$channels$1 INSTANCE = new ChannelEditorDialogController$getDisplayableChannels$channels$1();

    public ChannelEditorDialogController$getDisplayableChannels$channels$1() {
        super(1);
    }

    @NotNull
    public final Sequence<NotificationChannel> invoke(@NotNull NotificationChannelGroup notificationChannelGroup) {
        return SequencesKt___SequencesKt.filterNot(CollectionsKt___CollectionsKt.asSequence(notificationChannelGroup.getChannels()), AnonymousClass1.INSTANCE);
    }
}
