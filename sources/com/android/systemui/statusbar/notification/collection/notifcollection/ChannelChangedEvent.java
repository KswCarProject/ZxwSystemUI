package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.app.NotificationChannel;
import android.os.UserHandle;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifEvent.kt */
public final class ChannelChangedEvent extends NotifEvent {
    @NotNull
    public final NotificationChannel channel;
    public final int modificationType;
    @NotNull
    public final String pkgName;
    @NotNull
    public final UserHandle user;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChannelChangedEvent)) {
            return false;
        }
        ChannelChangedEvent channelChangedEvent = (ChannelChangedEvent) obj;
        return Intrinsics.areEqual((Object) this.pkgName, (Object) channelChangedEvent.pkgName) && Intrinsics.areEqual((Object) this.user, (Object) channelChangedEvent.user) && Intrinsics.areEqual((Object) this.channel, (Object) channelChangedEvent.channel) && this.modificationType == channelChangedEvent.modificationType;
    }

    public int hashCode() {
        return (((((this.pkgName.hashCode() * 31) + this.user.hashCode()) * 31) + this.channel.hashCode()) * 31) + Integer.hashCode(this.modificationType);
    }

    @NotNull
    public String toString() {
        return "ChannelChangedEvent(pkgName=" + this.pkgName + ", user=" + this.user + ", channel=" + this.channel + ", modificationType=" + this.modificationType + ')';
    }

    public ChannelChangedEvent(@NotNull String str, @NotNull UserHandle userHandle, @NotNull NotificationChannel notificationChannel, int i) {
        super((DefaultConstructorMarker) null);
        this.pkgName = str;
        this.user = userHandle;
        this.channel = notificationChannel;
        this.modificationType = i;
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onNotificationChannelModified(this.pkgName, this.user, this.channel, this.modificationType);
    }
}
